package ua.com.astone.acrm.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import ua.com.astone.acrm.model.Opportunity;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class OpportunityRepositoryTest {

    @Autowired
    private OpportunityRepository opportunityRepository;

    @Test
    void findByNameIgnoreCaseContainingOrStageIgnoreCaseContaining_works() {
        opportunityRepository.deleteAll();

        opportunityRepository.save(new Opportunity(null, "Big Deal", 80, BigDecimal.valueOf(20000), "Negotiation", null));
        opportunityRepository.save(new Opportunity(null, "Small Lead", 30, BigDecimal.valueOf(5000), "Initial", null));

        var result = opportunityRepository.findByNameIgnoreCaseContainingOrStageIgnoreCaseContaining(
                "deal", "initial", org.springframework.data.domain.PageRequest.of(0, 10));

        assertThat(result.getTotalElements()).isEqualTo(2);
        assertThat(result.getContent()).extracting("name").containsExactlyInAnyOrder("Big Deal", "Small Lead");
    }

    @Test
    void countByProbability_works() {
        opportunityRepository.deleteAll();

        opportunityRepository.save(new Opportunity(null, "A", 70, BigDecimal.valueOf(1000), "Stage1", null));
        opportunityRepository.save(new Opportunity(null, "B", 70, BigDecimal.valueOf(2000), "Stage1", null));
        opportunityRepository.save(new Opportunity(null, "C", 50, BigDecimal.valueOf(500), "Stage2", null));

        var stats = opportunityRepository.countByProbability();
        assertThat(stats).anySatisfy(dto -> {
            if (dto.getProbability() == 70) assertThat(dto.getCount()).isEqualTo(2L);
            if (dto.getProbability() == 50) assertThat(dto.getCount()).isEqualTo(1L);
        });
    }

    @Test
    void sumExpectedValueByStage_works() {
        opportunityRepository.deleteAll();

        opportunityRepository.save(new Opportunity(null, "A", 70, BigDecimal.valueOf(1000), "Stage1", null));
        opportunityRepository.save(new Opportunity(null, "B", 70, BigDecimal.valueOf(2000), "Stage1", null));
        opportunityRepository.save(new Opportunity(null, "C", 50, BigDecimal.valueOf(500), "Stage2", null));

        var stats = opportunityRepository.sumExpectedValueByStage();
        assertThat(stats).anySatisfy(dto -> {
            if (dto.getStage().equals("Stage1"))
                assertThat(dto.getSum()).isEqualByComparingTo(BigDecimal.valueOf(3000));
            if (dto.getStage().equals("Stage2"))
                assertThat(dto.getSum()).isEqualByComparingTo(BigDecimal.valueOf(500));
        });
    }
}

