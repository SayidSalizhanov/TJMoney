package ru.itis.impl.service.impl;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import ru.itis.impl.annotations.MayBeNull;
import ru.itis.impl.exception.FileProcessingException;
import ru.itis.impl.exception.not_found.GroupNotFoundException;
import ru.itis.impl.exception.not_found.UserNotFoundException;
import ru.itis.impl.model.Group;
import ru.itis.impl.model.Transaction;
import ru.itis.impl.model.User;
import ru.itis.impl.repository.GroupRepository;
import ru.itis.impl.repository.TransactionRepository;
import ru.itis.impl.repository.UserRepository;
import ru.itis.impl.service.AuthService;
import ru.itis.impl.service.CsvParsingService;
import ru.itis.impl.service.GroupService;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CsvParsingServiceImpl implements CsvParsingService {

    private final TransactionRepository transactionRepository;
    private final UserRepository userRepository;
    private final GroupRepository groupRepository;
    private final GroupService groupService;
    private final AuthService authService;

    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Override
    @Transactional
    public List<Long> parseCsv(@MayBeNull Long groupId, MultipartFile file) {

        User user = requireUserById(authService.getAuthenticatedUserId());
        Group group = groupId == null ? null : requireGroupById(groupId);

        if (group != null) {
            groupService.checkUserIsGroupMemberVoid(user, group);
        }

        if (file.isEmpty()) {
            throw new FileProcessingException("Файл пуст", HttpStatus.BAD_REQUEST);
        }

        if (!file.getOriginalFilename().endsWith(".csv")) {
            throw new FileProcessingException("Тип файла не поддерживается, используйте файлы типа .csv", HttpStatus.BAD_REQUEST);
        }

        List<Transaction> transactions = new ArrayList<>();

        try {
            Reader reader = new InputStreamReader(file.getInputStream());
            CSVReader csvReader = new CSVReader(reader);

            // Пропустить заголовок (если есть)
            String[] headers = csvReader.readNext();

            String[] nextLine;
            while ((nextLine = csvReader.readNext()) != null) {
                if (nextLine.length >= 5) {
                    try {
                        Transaction transaction = Transaction.builder()
                                .amount(parseAmount(nextLine[0]))
                                .category(nextLine[1])
                                .type(nextLine[2])
                                .dateTime(LocalDateTime.parse(nextLine[3], DATE_TIME_FORMATTER))
                                .description(nextLine[4])
                                .user(user)
                                .group(group)
                                .build();
                        transactions.add(transaction);
                    } catch (NumberFormatException e) {
                        System.err.println("Ошибка парсинга числа в строке: " + String.join(",", nextLine));
                    } catch (Exception e) {
                        System.err.println("Ошибка парсинга даты в строке: " + String.join(",", nextLine));
                    }
                }
            }
        } catch (IOException | CsvValidationException e) {
            throw new FileProcessingException("Не удалось прочитать содержимое файла", HttpStatus.BAD_REQUEST);
        }

        return transactionRepository.saveAll(transactions).stream()
                .map(Transaction::getId)
                .toList();
    }

    private Integer parseAmount(String value) {
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            throw new NumberFormatException("Некорректное значение amount: " + value);
        }
    }

    private User requireUserById(Long userId) {
        return userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException("Пользователь с таким id не найден"));
    }

    private Group requireGroupById(Long groupId) {
        return groupRepository.findById(groupId).orElseThrow(() -> new GroupNotFoundException("Группа с таким id не найдена"));
    }
}
