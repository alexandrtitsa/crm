package ua.com.astone.acrm.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ua.com.astone.acrm.model.Role;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(String name);
}
