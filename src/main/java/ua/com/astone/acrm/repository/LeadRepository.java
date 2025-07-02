package ua.com.astone.acrm.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import ua.com.astone.acrm.model.Lead;

public interface LeadRepository extends JpaRepository<Lead, Long> {
    Page<Lead> findBySourceIgnoreCaseContainingOrStatusIgnoreCaseContaining(String q, String q1, Pageable pageable);
}
