package ua.com.astone.acrm.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@SpringBootTest
class EmailServiceImplIntegrationTest {

    @Autowired
    private EmailService emailService;

    @Autowired
    private JavaMailSender mailSender;

    @AfterEach
    void resetMocks() {
        Mockito.reset(mailSender);
    }

    @TestConfiguration
    static class TestMailConfig {
        @Bean
        public JavaMailSender javaMailSender() {
            return Mockito.mock(JavaMailSender.class);
        }
    }

    @Test
    void sendSimpleMail_sendsCorrectMessage() {
        emailService.sendSimpleMail("test@example.com", "Test Subject", "Test Body");

        ArgumentCaptor<SimpleMailMessage> captor = ArgumentCaptor.forClass(SimpleMailMessage.class);
        verify(mailSender, times(1)).send(captor.capture());
        SimpleMailMessage sent = captor.getValue();

        assertThat(sent.getTo()).containsExactly("test@example.com");
        assertThat(sent.getSubject()).isEqualTo("Test Subject");
        assertThat(sent.getText()).isEqualTo("Test Body");
        assertThat(sent.getFrom()).isEqualTo("acrmka@gmail.com");
    }

    @Test
    void sendActivityNotification_sendsActivityMail() {
        emailService.sendActivityNotification(
                "user@example.com",
                "Call",
                "Follow up with client",
                LocalDateTime.of(2025, 7, 4, 14, 30)
        );

        ArgumentCaptor<SimpleMailMessage> captor = ArgumentCaptor.forClass(SimpleMailMessage.class);
        verify(mailSender, times(1)).send(captor.capture());
        SimpleMailMessage sent = captor.getValue();

        assertThat(sent.getTo()).containsExactly("user@example.com");
        assertThat(sent.getSubject()).isEqualTo("Нагадування про активність");
        assertThat(sent.getText()).contains("Call");
        assertThat(sent.getText()).contains("Follow up with client");
        assertThat(sent.getText()).contains("2025-07-04T14:30");
    }
}
