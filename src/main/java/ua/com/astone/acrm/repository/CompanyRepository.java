package ua.com.astone.acrm.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import ua.com.astone.acrm.model.Company;

public interface CompanyRepository extends JpaRepository<Company, Long> {
    Page<Company> findByNameIgnoreCaseContainingOrIndustryIgnoreCaseContaining(String q, String q1, Pageable pageable);
}
