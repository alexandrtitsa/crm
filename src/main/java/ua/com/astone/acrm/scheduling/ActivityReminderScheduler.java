package ua.com.astone.acrm.scheduling;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import ua.com.astone.acrm.service.ActivityService;
import ua.com.astone.acrm.service.EmailService;
import ua.com.astone.acrm.dto.activity.ActivityResponse;
import ua.com.astone.acrm.service.ContactService;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Component
@RequiredArgsConstructor
public class ActivityReminderScheduler {

    private final ActivityService activityService;
    private final EmailService emailService;
    private final ContactService contactService;

    // Запускаємо щогодини
    @Scheduled(cron = "0 0 * * * *")
    public void sendReminders() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime nextDay = now.plusDays(1).truncatedTo(ChronoUnit.DAYS);

        List<ActivityResponse> activities = activityService.findAll();
        for (ActivityResponse act : activities) {
            if (act.getDateTime() != null &&
                    act.getDateTime().isAfter(now) &&
                    act.getDateTime().isBefore(nextDay)) {

                String contactEmail = contactService.findById(act.getContactId()).getEmail();
                if (contactEmail != null && !contactEmail.isBlank()) {
                    emailService.sendActivityNotification(
                            contactEmail,
                            act.getType(),
                            act.getDescription(),
                            act.getDateTime()
                    );
                }
            }
        }
    }
}
