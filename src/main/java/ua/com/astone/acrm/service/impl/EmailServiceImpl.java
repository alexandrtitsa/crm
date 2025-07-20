package ua.com.astone.acrm.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import ua.com.astone.acrm.service.EmailService;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {
    private final JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String emailSend;

    @Override
    public void sendSimpleMail(String to, String subject, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(emailSend);
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);
        mailSender.send(message);
    }

    @Override
    public void sendActivityNotification(String to, String activityType, String description, LocalDateTime dateTime) {
        String body = "Запланована активність: " + activityType +
                "\nОпис: " + (description == null ? "" : description) +
                "\nДата: " + (dateTime == null ? "" : dateTime.toString());
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject("Нагадування про активність");
        message.setText(body);
        mailSender.send(message);
    }
}
