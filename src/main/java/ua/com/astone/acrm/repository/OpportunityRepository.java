package ua.com.astone.acrm.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ua.com.astone.acrm.dto.analytics.ProbabilityCountDto;
import ua.com.astone.acrm.dto.analytics.StageValueDto;
import ua.com.astone.acrm.model.Opportunity;

import java.util.List;

public interface OpportunityRepository extends JpaRepository<Opportunity, Long> {
    Page<Opportunity> findByNameIgnoreCaseContainingOrStageIgnoreCaseContaining(String q, String q1, Pageable pageable);
    @Query("SELECT new ua.com.astone.acrm.dto.analytics.ProbabilityCountDto(o.probability, COUNT(o)) FROM Opportunity o GROUP BY o.probability")
    List<ProbabilityCountDto> countByProbability();

    @Query("SELECT new ua.com.astone.acrm.dto.analytics.StageValueDto(o.stage, SUM(o.expectedValue)) FROM Opportunity o GROUP BY o.stage")
    List<StageValueDto> sumExpectedValueByStage();
}
