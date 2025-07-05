package ua.com.astone.acrm.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import ua.com.astone.acrm.model.Activity;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class ActivityRepositoryTest {

    @Autowired
    private ActivityRepository activityRepository;

    @Test
    void findByTypeIgnoreCaseContainingOrDescriptionIgnoreCaseContaining_works() {
        activityRepository.deleteAll();

        activityRepository.save(Activity.builder()
                .type("Call")
                .dateTime(LocalDateTime.now())
                .description("Call with Ivan")
                .build());
        activityRepository.save(Activity.builder()
                .type("Meeting")
                .dateTime(LocalDateTime.now())
                .description("Strategy session")
                .build());

        var result = activityRepository.findByTypeIgnoreCaseContainingOrDescriptionIgnoreCaseContaining(
                "call", "strategy", org.springframework.data.domain.PageRequest.of(0, 10));

        assertThat(result.getTotalElements()).isEqualTo(2);
        assertThat(result.getContent()).extracting("type").containsExactlyInAnyOrder("Call", "Meeting");
    }
}
