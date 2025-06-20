package ua.com.astone.acrm.config;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.flywaydb.core.Flyway;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Profile("dev")
@RequiredArgsConstructor
public class FlywayDevCleaner {

    private final Flyway flyway;

    @PostConstruct
    public void cleanAndMigrate() {
        flyway.clean();
        flyway.migrate();
    }
}
