package ru.itis.impl.scheduler;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import ru.itis.impl.model.Reminder;
import ru.itis.impl.repository.ReminderRepository;
import ru.itis.impl.service.impl.EmailService;

import java.time.LocalDateTime;
import java.util.List;

import static ru.itis.impl.enums.ReminderStatusEnum.*;

@Component
@RequiredArgsConstructor
public class ReminderScheduler {

    private final ReminderRepository reminderRepository;
    private final EmailService emailService;

    @Scheduled(fixedRate = 10000)
    public void sendReminders() {
        LocalDateTime now = LocalDateTime.now();
        List<Reminder> reminders = reminderRepository.findAllBySendAtBeforeAndStatusEquals(
                now, CREATED.getValue());

        for (Reminder reminder : reminders) {
            try {
                emailService.sendReminder(
                        reminder.getUser().getEmail(),
                        "Напоминание: " + reminder.getTitle(),
                        reminder.getMessage()
                );
                reminder.setStatus(SENT.getValue());
                reminderRepository.save(reminder);
            } catch (Exception e) {
                reminder.setStatus(NOT_SENT.getValue());
                reminderRepository.save(reminder);
            }
        }
    }
} 