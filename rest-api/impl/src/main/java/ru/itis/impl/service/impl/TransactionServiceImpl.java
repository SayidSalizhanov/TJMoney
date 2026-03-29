package ru.itis.impl.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.itis.dto.request.transaction.TransactionCreateRequest;
import ru.itis.dto.request.transaction.TransactionSettingsRequest;
import ru.itis.dto.response.transaction.TransactionListResponse;
import ru.itis.dto.response.transaction.TransactionSettingsResponse;
import ru.itis.impl.annotations.MayBeNull;
import ru.itis.impl.exception.AccessDeniedException;
import ru.itis.impl.exception.DateTimeOperationException;
import ru.itis.impl.exception.not_found.GroupNotFoundException;
import ru.itis.impl.exception.not_found.TransactionNotFoundException;
import ru.itis.impl.exception.not_found.UserNotFoundException;
import ru.itis.impl.mapper.TransactionMapper;
import ru.itis.impl.model.Group;
import ru.itis.impl.model.GroupMember;
import ru.itis.impl.model.Transaction;
import ru.itis.impl.model.User;
import ru.itis.impl.repository.GroupMemberRepository;
import ru.itis.impl.repository.GroupRepository;
import ru.itis.impl.repository.TransactionRepository;
import ru.itis.impl.repository.UserRepository;
import ru.itis.impl.service.AuthService;
import ru.itis.impl.service.TransactionService;
import ru.itis.impl.service.UserGroupRequireService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.web.client.RestTemplate;
import ru.itis.dto.request.transaction.TransactionPredictRequest;
import ru.itis.dto.response.transaction.TransactionPredictResponse;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.*;

import static ru.itis.impl.enums.GroupMemberStatusEnum.ADMIN;

@Service
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService {

    private final TransactionRepository transactionRepository;
    private final TransactionMapper transactionMapper;

    private final AuthService authService;
    private final GroupMemberRepository groupMemberRepository;

    private final UserGroupRequireService userGroupRequireService;

    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    @Transactional
    public List<Map<String, Integer>> getGroupTransactionsGenerals(Long groupId, String period) {
        List<Transaction> transactions = getGroupTransactions(groupId, period);
        return getTransactionsGeneralsMaps(transactions);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Map<String, Integer>> getUserTransactionsGenerals(Long userId, String period) {
        List<Transaction> transactions = getUserTransactions(userId, period);
        return getTransactionsGeneralsMaps(transactions);
    }

    @Override
    @Transactional(readOnly = true)
    public TransactionSettingsResponse getById(Long id) {
        User user = userGroupRequireService.requireUserById(authService.getAuthenticatedUserId());
        Transaction transaction = requireById(id);

        checkAccessToTransactionGranted(transaction, user);

        return transactionMapper.toTransactionSettingsResponse(transaction);
    }

    @Override
    @Transactional
    public void updateInfo(Long id, TransactionSettingsRequest request) {
        User user = userGroupRequireService.requireUserById(authService.getAuthenticatedUserId());
        Transaction transaction = requireById(id);

        checkAccessToTransactionGranted(transaction, user);

        transaction.setAmount(request.amount());
        transaction.setType(request.type());
        transaction.setCategory(request.category());
        transaction.setDescription(request.description());
        transactionRepository.save(transaction);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        User user = userGroupRequireService.requireUserById(authService.getAuthenticatedUserId());
        Transaction transaction = requireById(id);

        checkAccessToTransactionGranted(transaction, user);

        transactionRepository.delete(transaction);
    }

    @Override
    @Transactional(readOnly = true)
    public List<TransactionListResponse> getAll(@MayBeNull Long groupId, Integer page, Integer amountPerPage) {
        User user = userGroupRequireService.requireUserById(authService.getAuthenticatedUserId());
        Group group = groupId == null ? null : userGroupRequireService.requireGroupById(groupId);

        List<Transaction> transactions;

        Pageable pageable = PageRequest.of(page, amountPerPage, Sort.by("amount"));

        if (group == null) transactions = transactionRepository.findAllByUserAndGroupIsNull(user, pageable).getContent();
        else {
            checkUserIsGroupMemberVoid(user, group);
            transactions = transactionRepository.findAllByGroup(group, pageable).getContent();
        }

        return transactions.stream()
                .map(transactionMapper::toTransactionListResponse)
                .toList();
    }

    @Override
    @Transactional
    public Long create(@MayBeNull Long groupId, TransactionCreateRequest request) {
        if (request.dateTime().isAfter(LocalDateTime.now())) throw new DateTimeOperationException("Транзакция не может быть совершена в будущем");

        User user = userGroupRequireService.requireUserById(authService.getAuthenticatedUserId());
        Group group = groupId == null ? null : userGroupRequireService.requireGroupById(groupId);

        Transaction transaction = Transaction.builder()
                .amount(request.amount())
                .category(request.category())
                .type(request.type())
                .dateTime(request.dateTime())
                .description(request.description())
                .group(group)
                .user(user)
                .build();

        return transactionRepository.save(transaction).getId();
    }

    @Override
    @Transactional(readOnly = true)
    public TransactionPredictResponse predictUserExpenses() {
        Long userId = authService.getAuthenticatedUserId();
        System.out.println("=== DEBUG: predictUserExpenses для пользователя " + userId + " ===");
        
        // Собираем траты пользователя за последние 12 месяцев по категориям
        List<Transaction> transactions = getUserTransactions(userId, "year");
        System.out.println("Найдено транзакций: " + transactions.size());
        
        Map<YearMonth, Map<String, Integer>> monthCategoryMap = new HashMap<>();
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime yearAgo = now.minusYears(1);
        
        for (Transaction t : transactions) {
            if (!"Расход".equalsIgnoreCase(t.getType())) continue;
            if (t.getDateTime().isBefore(yearAgo)) continue;
            YearMonth ym = YearMonth.from(t.getDateTime());
            monthCategoryMap.putIfAbsent(ym, new HashMap<>());
            Map<String, Integer> catMap = monthCategoryMap.get(ym);
            catMap.put(t.getCategory(), catMap.getOrDefault(t.getCategory(), 0) + t.getAmount());
        }
        
        System.out.println("Месяцев с расходами: " + monthCategoryMap.size());
        monthCategoryMap.forEach((ym, data) -> {
            System.out.println("Месяц " + ym + ": " + data);
        });
        
        // Формируем список из 12 месяцев (даже если по месяцу нет трат)
        List<TransactionPredictRequest.MonthData> months = new ArrayList<>();
        YearMonth thisMonth = YearMonth.from(now);
        for (int i = 11; i >= 0; i--) {
            YearMonth ym = thisMonth.minusMonths(i);
            Map<String, Integer> cats = monthCategoryMap.getOrDefault(ym, new HashMap<>());
            months.add(new TransactionPredictRequest.MonthData(ym.getMonthValue(), cats));
        }
        TransactionPredictRequest req = new TransactionPredictRequest(months);
        String url = "http://localhost:8000/api/predict-expenses/";
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            String json = objectMapper.writeValueAsString(req);
            System.out.println("Запрос к ML-сервису: " + json);
            HttpEntity<String> entity = new HttpEntity<>(json, headers);
            TransactionPredictResponse response = restTemplate.postForObject(url, entity, TransactionPredictResponse.class);
            System.out.println("Ответ от ML-сервиса: " + response);
            
            // Добавляем исторические данные за последние 6 месяцев
            Map<String, Double> historicalExpenses = new HashMap<>();
            YearMonth currentMonth = YearMonth.from(now);
            for (int i = 5; i >= 0; i--) {
                YearMonth ym = currentMonth.minusMonths(i);
                String monthKey = ym.getMonth().getDisplayName(java.time.format.TextStyle.SHORT, java.util.Locale.getDefault()) + " " + ym.getYear();
                Map<String, Integer> monthData = monthCategoryMap.getOrDefault(ym, new HashMap<>());
                // Считаем общий итог за месяц
                double totalForMonth = monthData.values().stream().mapToDouble(Integer::doubleValue).sum();
                historicalExpenses.put(monthKey, totalForMonth);
                System.out.println("Исторические данные для " + monthKey + ": " + totalForMonth);
            }
            
            System.out.println("Итоговые исторические данные: " + historicalExpenses);
            
            // Создаем новый ответ с историческими данными
            // Используем данные из response, если они есть, иначе создаем пустые
            Map<String, Double> predictedExpenses = response != null && response.getPredicted_expenses() != null 
                ? response.getPredicted_expenses() : new HashMap<>();
            Double total = response != null ? response.getTotal() : 0.0;
            String message = response != null ? response.getMessage() : "Прогноз недоступен";
            
            TransactionPredictResponse responseWithHistory = new TransactionPredictResponse(
                predictedExpenses,
                total,
                message,
                historicalExpenses
            );
            
            System.out.println("Финальный ответ: " + responseWithHistory);
            return responseWithHistory;
        } catch (Exception e) {
            System.out.println("Ошибка при обращении к ML-сервису: " + e.getMessage());
            e.printStackTrace();
            // Если ML-сервис недоступен или возвращает ошибку, создаем ответ только с историческими данными
            Map<String, Double> historicalExpenses = new HashMap<>();
            YearMonth currentMonth = YearMonth.from(now);
            for (int i = 5; i >= 0; i--) {
                YearMonth ym = currentMonth.minusMonths(i);
                String monthKey = ym.getMonth().getDisplayName(java.time.format.TextStyle.SHORT, java.util.Locale.getDefault()) + " " + ym.getYear();
                Map<String, Integer> monthData = monthCategoryMap.getOrDefault(ym, new HashMap<>());
                double totalForMonth = monthData.values().stream().mapToDouble(Integer::doubleValue).sum();
                historicalExpenses.put(monthKey, totalForMonth);
                System.out.println("Fallback исторические данные для " + monthKey + ": " + totalForMonth);
            }
            
            System.out.println("Fallback итоговые исторические данные: " + historicalExpenses);
            
            return new TransactionPredictResponse(
                new HashMap<>(),
                0.0,
                "ML-сервис недоступен. Показаны только исторические данные.",
                historicalExpenses
            );
        }
    }

    private List<Map<String, Integer>> getTransactionsGeneralsMaps(List<Transaction> transactions) {
        Map<String, Integer> mapOfIncomeTransactions = new HashMap<>();
        Map<String, Integer> mapOfExpenseTransactions = new HashMap<>();

        mapOfIncomeTransactions.put("Заработная плата", 0);
        mapOfIncomeTransactions.put("Прибыль от бизнеса", 0);
        mapOfIncomeTransactions.put("Дивиденды", 0);
        mapOfIncomeTransactions.put("Аренда", 0);
        mapOfIncomeTransactions.put("Премии и бонусы", 0);
        mapOfIncomeTransactions.put("Интересы", 0);
        mapOfIncomeTransactions.put("Пенсии и пособия", 0);
        mapOfIncomeTransactions.put("Другое", 0);

        mapOfExpenseTransactions.put("Еда и напитки", 0);
        mapOfExpenseTransactions.put("Транспорт", 0);
        mapOfExpenseTransactions.put("Жилье", 0);
        mapOfExpenseTransactions.put("Развлечения", 0);
        mapOfExpenseTransactions.put("Одежда", 0);
        mapOfExpenseTransactions.put("Здоровье", 0);
        mapOfExpenseTransactions.put("Образование", 0);
        mapOfExpenseTransactions.put("Другое", 0);

        for (Transaction transaction : transactions) {
            if (transaction.getType().equalsIgnoreCase("Доход")) {
                mapOfIncomeTransactions.merge(transaction.getCategory(), transaction.getAmount(), Integer::sum);
            }
            else {
                mapOfExpenseTransactions.merge(transaction.getCategory(), transaction.getAmount(), Integer::sum);
            }
        }

        mapOfIncomeTransactions.put("Доход", mapOfIncomeTransactions.values().stream().mapToInt(i -> i).sum());
        mapOfExpenseTransactions.put("Расход", mapOfExpenseTransactions.values().stream().mapToInt(i -> i).sum());

        List<Map<String, Integer>> maps = new ArrayList<>();
        maps.add(mapOfIncomeTransactions);
        maps.add(mapOfExpenseTransactions);

        return maps;
    }

    @Transactional
    protected List<Transaction> getGroupTransactions(Long groupId, String period) {
        List<Transaction> transactions;
        Group group = userGroupRequireService.requireGroupById(groupId);

        transactions = switch (period) {
            case "day" -> transactionRepository.findByGroupWithPeriod(group, LocalDateTime.now().minusDays(1));
            case "month" -> transactionRepository.findByGroupWithPeriod(group, LocalDateTime.now().minusMonths(1));
            case "year" -> transactionRepository.findByGroupWithPeriod(group, LocalDateTime.now().minusYears(1));
            default -> transactionRepository.findAllByGroup(group);
        };

        return transactions;
    }

    @Transactional
    protected List<Transaction> getUserTransactions(Long userId, String period) {
        List<Transaction> transactions;
        User user = userGroupRequireService.requireUserById(userId);

        transactions = switch (period) {
            case "day" -> transactionRepository.findByUserWithPeriod(user, LocalDateTime.now().minusDays(1));
            case "month" -> transactionRepository.findByUserWithPeriod(user, LocalDateTime.now().minusMonths(1));
            case "year" -> transactionRepository.findByUserWithPeriod(user, LocalDateTime.now().minusYears(1));
            default -> transactionRepository.findAllByUserAndGroupIsNull(user);
        };

        return transactions;
    }

    private Transaction requireById(Long id) {
        return transactionRepository.findById(id).orElseThrow(() -> new TransactionNotFoundException("Транзакция с таким id не найдена"));
    }

    private void checkUserIsTransactionOwner(Transaction transaction, User user) {
        if (!Objects.equals(transaction.getUser().getId(), user.getId())) throw new AccessDeniedException("Доступ к личной транзакции имеет только ее владелец");
    }

    private void checkAccessToTransactionGranted(Transaction transaction, User user) {
        Group group = transaction.getGroup();

        if (group == null) {
            checkUserIsTransactionOwner(transaction, user);
        }
        else {
            if (!Objects.equals(transaction.getUser().getId(), user.getId()) && checkUserIsGroupAdminBoolean(user, group)) {
                throw new AccessDeniedException("Доступ к транзакции имеет только ее владелец или админ группы");
            }
        }
    }

    public void checkUserIsGroupMemberVoid(User user, Group group) {
        Optional<GroupMember> optionalGroupMember = groupMemberRepository.findByGroupAndUser(group, user);
        if (optionalGroupMember.isEmpty()) throw new AccessDeniedException("Доступ к данным имеют только участники группы");
    }

    public boolean checkUserIsGroupAdminBoolean(User user, Group group) {
        GroupMember groupMember = checkUserIsGroupMember(user, group);
        return groupMember.getRole().equalsIgnoreCase(ADMIN.getValue());
    }

    private GroupMember checkUserIsGroupMember(User user, Group group) {
        Optional<GroupMember> optionalGroupMember = groupMemberRepository.findByGroupAndUser(group, user);
        if (optionalGroupMember.isEmpty()) throw new AccessDeniedException("Доступ к данным имеют только участники группы");
        return optionalGroupMember.get();
    }
}
