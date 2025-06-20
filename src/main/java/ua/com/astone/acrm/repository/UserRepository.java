package ua.com.astone.acrm.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ua.com.astone.acrm.model.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    boolean existsByEmail(String email);
}
