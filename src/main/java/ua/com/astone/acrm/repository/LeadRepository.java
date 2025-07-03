package ua.com.astone.acrm.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ua.com.astone.acrm.dto.analytics.StatusCountDto;
import ua.com.astone.acrm.model.Lead;

import java.util.List;

public interface LeadRepository extends JpaRepository<Lead, Long> {
    Page<Lead> findBySourceIgnoreCaseContainingOrStatusIgnoreCaseContaining(String q, String q1, Pageable pageable);
    @Query("SELECT new ua.com.astone.acrm.dto.analytics.StatusCountDto(l.status, COUNT(l)) FROM Lead l GROUP BY l.status")
    List<StatusCountDto> countByStatus();

}
