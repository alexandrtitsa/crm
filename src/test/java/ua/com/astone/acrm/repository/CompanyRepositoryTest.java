package ua.com.astone.acrm.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import ua.com.astone.acrm.model.Company;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class CompanyRepositoryTest {

    @Autowired
    private CompanyRepository companyRepository;

    @Test
    void findByNameIgnoreCaseContainingOrIndustryIgnoreCaseContaining_works() {
        companyRepository.deleteAll();

        companyRepository.save(Company.builder()
                .name("ACrm Soft")
                .industry("Software")
                .build());
        companyRepository.save(Company.builder()
                .name("BizTech")
                .industry("Finance")
                .build());

        var result = companyRepository.findByNameIgnoreCaseContainingOrIndustryIgnoreCaseContaining(
                "crm", "finance", org.springframework.data.domain.PageRequest.of(0, 10));

        assertThat(result.getTotalElements()).isEqualTo(2);
        assertThat(result.getContent()).extracting("name").containsExactlyInAnyOrder("ACrm Soft", "BizTech");
    }
}
