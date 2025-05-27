package ru.itis.impl.service.impl;

import lombok.RequiredArgsConstructor;
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
import ru.itis.impl.model.Transaction;
import ru.itis.impl.model.User;
import ru.itis.impl.repository.GroupRepository;
import ru.itis.impl.repository.TransactionRepository;
import ru.itis.impl.repository.UserRepository;
import ru.itis.impl.service.GroupService;
import ru.itis.impl.service.TransactionService;

import java.time.LocalDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService {

    private final TransactionRepository transactionRepository;
    private final TransactionMapper transactionMapper;

    private final UserRepository userRepository;
    private final GroupRepository groupRepository;
    private final GroupService groupService;

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
    public TransactionSettingsResponse getById(Long id, Long userId) {
        User user = requireUserById(userId);
        Transaction transaction = requireById(id);

        checkAccessToTransactionGranted(transaction, user);

        return transactionMapper.toTransactionSettingsResponse(transaction);
    }

    @Override
    @Transactional
    public void updateInfo(Long id, Long userId, TransactionSettingsRequest request) {
        User user = requireUserById(userId);
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
    public void delete(Long id, Long userId) {
        User user = requireUserById(userId);
        Transaction transaction = requireById(id);

        checkAccessToTransactionGranted(transaction, user);

        transactionRepository.delete(transaction);
    }

    @Override
    @Transactional(readOnly = true)
    public List<TransactionListResponse> getAll(Long userId, @MayBeNull Long groupId, Integer page, Integer amountPerPage, String sort) {
        User user = requireUserById(userId);
        Group group = groupId == null ? null : requireGroupById(groupId);

        List<Transaction> transactions;

        if (group == null) transactions = transactionRepository.findAllByUser(user);
        else {
            groupService.checkUserIsGroupMemberVoid(user, group);
            transactions = transactionRepository.findAllByGroup(group);
        }

        return transactions.stream()
                .map(transactionMapper::toTransactionListResponse)
                .toList();
    }

    @Override
    @Transactional
    public Long create(Long userId, @MayBeNull Long groupId, TransactionCreateRequest request) {
        if (request.dateTime().isAfter(LocalDateTime.now())) throw new DateTimeOperationException("Транзакция не может быть совершена в будущем");

        User user = requireUserById(userId);
        Group group = groupId == null ? null : requireGroupById(groupId);

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

        List<Map<String, Integer>> maps = new ArrayList<>();
        maps.add(mapOfIncomeTransactions);
        maps.add(mapOfExpenseTransactions);

        return maps;
    }

    @Transactional
    protected List<Transaction> getGroupTransactions(Long groupId, String period) {
        List<Transaction> transactions;
        Group group = requireGroupById(groupId);

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
        User user = requireUserById(userId);

        transactions = switch (period) {
            case "day" -> transactionRepository.findByUserWithPeriod(user, LocalDateTime.now().minusDays(1));
            case "month" -> transactionRepository.findByUserWithPeriod(user, LocalDateTime.now().minusMonths(1));
            case "year" -> transactionRepository.findByUserWithPeriod(user, LocalDateTime.now().minusYears(1));
            default -> transactionRepository.findAllByUser(user);
        };

        return transactions;
    }

    private Transaction requireById(Long id) {
        return transactionRepository.findById(id).orElseThrow(() -> new TransactionNotFoundException("Транзакция с таким id не найдена"));
    }

    private User requireUserById(Long userId) {
        return userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException("Пользователь с таким id не найден"));
    }

    private Group requireGroupById(Long groupId) {
        return groupRepository.findById(groupId).orElseThrow(() -> new GroupNotFoundException("Группа с таким id не найдена"));
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
            if (!Objects.equals(transaction.getUser().getId(), user.getId()) && groupService.checkUserIsGroupAdminBoolean(user, group)) {
                throw new AccessDeniedException("Доступ к транзакции имеет только ее владелец или админ группы");
            }
        }
    }
}
