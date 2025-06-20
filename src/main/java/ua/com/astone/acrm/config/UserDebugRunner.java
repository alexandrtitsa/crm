package ua.com.astone.acrm.config;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import ua.com.astone.acrm.model.User;
import ua.com.astone.acrm.repository.UserRepository;

@Component
@Profile("dev") // тільки в dev!
@RequiredArgsConstructor
public class UserDebugRunner implements CommandLineRunner {

    private final UserRepository userRepository;
    private static final Logger log = LoggerFactory.getLogger(UserDebugRunner.class);

    @Override
    public void run(String... args) {
        log.info("🧩 USERS IN DATABASE:");
        userRepository.findAll().forEach(user -> {
            String roles = user.getRoles().stream()
                    .map(r -> r.getName().replace("ROLE_", ""))
                    .reduce((a, b) -> a + ", " + b).orElse("none");

            log.info("📧 {} | 🔑 {} | 👤 Roles: {}", user.getEmail(), user.getPassword(), roles);
        });
    }
}
