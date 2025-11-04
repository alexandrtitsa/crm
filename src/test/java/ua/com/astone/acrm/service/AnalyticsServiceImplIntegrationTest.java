package ua.com.astone.acrm.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import ua.com.astone.acrm.dto.analytics.ProbabilityCountDto;
import ua.com.astone.acrm.dto.analytics.StageValueDto;
import ua.com.astone.acrm.dto.analytics.StatusCountDto;
import ua.com.astone.acrm.model.Lead;
import ua.com.astone.acrm.model.Opportunity;
import ua.com.astone.acrm.repository.LeadRepository;
import ua.com.astone.acrm.repository.OpportunityRepository;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
@ActiveProfiles("test")
@Transactional
class AnalyticsServiceImplIntegrationTest {

    @TestConfiguration
    static class TestMailConfig {
        @Bean
        public JavaMailSender javaMailSender() {
            return Mockito.mock(JavaMailSender.class);
        }
    }

    @Autowired
    private AnalyticsService analyticsService;

    @Autowired
    private LeadRepository leadRepository;

    @Autowired
    private OpportunityRepository opportunityRepository;

    @BeforeEach
    void setUp() {
        opportunityRepository.deleteAll();
        leadRepository.deleteAll();

        leadRepository.saveAll(List.of(
                new Lead(null, "web", "new", null, null),
                new Lead(null, "web", "contacted", null, null),
                new Lead(null, "email", "new", null, null)
        ));

        opportunityRepository.saveAll(List.of(
                new Opportunity(null, "Deal1", 70, BigDecimal.valueOf(1000), "Initial", null),
                new Opportunity(null, "Deal2", 50, BigDecimal.valueOf(500), "Negotiation", null),
                new Opportunity(null, "Deal3", 70, BigDecimal.valueOf(1500), "Initial", null)
        ));
    }

    @Test
    void countLeadsByStatus_returnsCorrectCounts() {
        List<StatusCountDto> result = analyticsService.countLeadsByStatus();

        assertThat(result).anySatisfy(dto -> {
            if (dto.status().equals("new")) assertThat(dto.count()).isEqualTo(2L);
            if (dto.status().equals("contacted")) assertThat(dto.count()).isEqualTo(1L);
        });
    }

    @Test
    void countOpportunitiesByProbability_returnsCorrectCounts() {
        List<ProbabilityCountDto> result = analyticsService.countOpportunitiesByProbability();

        assertThat(result).anySatisfy(dto -> {
            if (dto.getProbability() == 70) assertThat(dto.getCount()).isEqualTo(2L);
            if (dto.getProbability() == 50) assertThat(dto.getCount()).isEqualTo(1L);
        });
    }

    @Test
    void sumExpectedValueByStage_returnsCorrectSums() {
        List<StageValueDto> result = analyticsService.sumExpectedValueByStage();

        assertThat(result).anySatisfy(dto -> {
            if (dto.getStage().equals("Initial"))
                assertThat(dto.getSum()).isEqualByComparingTo(BigDecimal.valueOf(2500));
            if (dto.getStage().equals("Negotiation"))
                assertThat(dto.getSum()).isEqualByComparingTo(BigDecimal.valueOf(500));
        });
    }
}
