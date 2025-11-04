package ua.com.astone.acrm.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.containers.PostgreSQLContainer;
import ua.com.astone.acrm.dto.activity.ActivityRequest;
import ua.com.astone.acrm.dto.activity.ActivityResponse;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@SpringBootTest
@Testcontainers
class ActivityServiceIntegrationTest {

    @Container
    static final PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16.3")
            .withDatabaseName("testdb")
            .withUsername("testuser")
            .withPassword("testpass");

    @Autowired
    private ActivityService activityService;

    @Autowired
    private EmailService emailService;

    @TestConfiguration
    static class EmailServiceTestConfig {
        @Bean
        public EmailService emailService() {
            return Mockito.mock(EmailService.class);
        }
    }

    @Test
    void createAndFindActivity() {
        ActivityRequest request = ActivityRequest.builder()
                .type("Call")
                .dateTime(LocalDateTime.now().plusDays(1))
                .description("Follow up")
                .contactId(null)
                .opportunityId(null)
                .build();

        ActivityResponse created = activityService.create(request);

        assertThat(created.getId()).isNotNull();
        assertThat(created.getType()).isEqualTo("Call");
        assertThat(created.getDescription()).isEqualTo("Follow up");

        verify(emailService, times(1))
                .sendActivityNotification(anyString(), eq("Call"), eq("Follow up"), any());
    }

    @Test
    void updateActivity() {
        ActivityRequest request = ActivityRequest.builder()
                .type("Meeting")
                .dateTime(LocalDateTime.now().plusDays(2))
                .description("Strategy session")
                .build();

        ActivityResponse created = activityService.create(request);

        ActivityRequest updateReq = ActivityRequest.builder()
                .type("Updated Meeting")
                .dateTime(LocalDateTime.now().plusDays(3))
                .description("Updated desc")
                .build();

        ActivityResponse updated = activityService.update(created.getId(), updateReq);

        assertThat(updated.getType()).isEqualTo("Updated Meeting");
        assertThat(updated.getDescription()).isEqualTo("Updated desc");
    }

    @Test
    void deleteActivity() {
        ActivityRequest req = ActivityRequest.builder()
                .type("DeleteMe")
                .dateTime(LocalDateTime.now())
                .description("To be deleted")
                .build();

        ActivityResponse created = activityService.create(req);
        Long id = created.getId();

        activityService.delete(id);

        Assertions.assertThrows(IllegalArgumentException.class, () -> activityService.findById(id));
    }

    @Test
    void findAllActivities() {
        List<ActivityResponse> all = activityService.findAll();
        assertThat(all).isNotNull();
    }
}
