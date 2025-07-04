package ua.com.astone.acrm.service;

import java.time.LocalDateTime;

public interface EmailService {
    void sendSimpleMail(String to, String subject, String text);
    void sendActivityNotification(String to, String activityType, String description, LocalDateTime dateTime);
}
