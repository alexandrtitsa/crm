package ua.com.astone.acrm.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import ua.com.astone.acrm.model.Activity;

public interface ActivityRepository extends JpaRepository<Activity, Long> {
    Page<Activity> findByTypeIgnoreCaseContainingOrDescriptionIgnoreCaseContaining(String q, String q1, Pageable pageable);
}
