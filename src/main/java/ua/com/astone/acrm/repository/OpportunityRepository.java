package ua.com.astone.acrm.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ua.com.astone.acrm.model.Opportunity;

public interface OpportunityRepository extends JpaRepository<Opportunity, Long> {
}
