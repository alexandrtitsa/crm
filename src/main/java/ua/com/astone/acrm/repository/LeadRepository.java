package ua.com.astone.acrm.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ua.com.astone.acrm.model.Lead;

public interface LeadRepository extends JpaRepository<Lead, Long> {
}
