package ru.itis.impl.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.itis.impl.exception.not_found.GroupNotFoundException;
import ru.itis.impl.exception.not_found.UserNotFoundException;
import ru.itis.impl.model.Group;
import ru.itis.impl.model.Transaction;
import ru.itis.impl.model.User;
import ru.itis.impl.repository.GroupRepository;
import ru.itis.impl.repository.TransactionRepository;
import ru.itis.impl.repository.UserRepository;
import ru.itis.impl.service.TransactionService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService {

    private final TransactionRepository transactionRepository;

    private final UserRepository userRepository;
    private final GroupRepository groupRepository;

    @Override
    @Transactional
    public List<Map<String, Integer>> getGroupTransactionsGenerals(Long groupId, String period) {
        List<Transaction> transactions = getGroupTransactions(groupId, period);
        return getTransactionsGeneralsMaps(transactions);
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

    private User requireUserById(Long userId) {
        return userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException("Пользователь с таким id не найден"));
    }

    private Group requireGroupById(Long groupId) {
        return groupRepository.findById(groupId).orElseThrow(() -> new GroupNotFoundException("Группа с таким id не найдена"));
    }
}
