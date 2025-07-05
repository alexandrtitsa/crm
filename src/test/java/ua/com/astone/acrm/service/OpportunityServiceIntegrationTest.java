package ua.com.astone.acrm.service;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.containers.PostgreSQLContainer;
import ua.com.astone.acrm.dto.opportunity.OpportunityRequest;
import ua.com.astone.acrm.dto.opportunity.OpportunityResponse;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Testcontainers
class OpportunityServiceIntegrationTest {

    @Container
    static final PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16.3")
            .withDatabaseName("testdb")
            .withUsername("testuser")
            .withPassword("testpass");

    @Autowired
    private OpportunityService opportunityService;

    @Test
    void createAndFindOpportunity() {
        OpportunityRequest req = OpportunityRequest.builder()
                .name("Test Opportunity")
                .probability(80)
                .expectedValue(BigDecimal.valueOf(12000))
                .stage("Proposal")
                .leadId(null)
                .build();

        OpportunityResponse created = opportunityService.create(req);

        assertThat(created.getId()).isNotNull();
        assertThat(created.getName()).isEqualTo("Test Opportunity");

        OpportunityResponse found = opportunityService.findById(created.getId());
        assertThat(found.getStage()).isEqualTo("Proposal");
    }

    @Test
    void updateOpportunity() {
        OpportunityRequest req = OpportunityRequest.builder()
                .name("ToUpdate")
                .probability(40)
                .expectedValue(BigDecimal.valueOf(3000))
                .stage("Qualification")
                .build();

        OpportunityResponse created = opportunityService.create(req);

        OpportunityRequest updateReq = OpportunityRequest.builder()
                .name("Updated Opportunity")
                .probability(90)
                .expectedValue(BigDecimal.valueOf(40000))
                .stage("Closed Won")
                .build();

        OpportunityResponse updated = opportunityService.update(created.getId(), updateReq);

        assertThat(updated.getName()).isEqualTo("Updated Opportunity");
        assertThat(updated.getStage()).isEqualTo("Closed Won");
        assertThat(updated.getExpectedValue()).isEqualTo(BigDecimal.valueOf(40000));
    }

    @Test
    void deleteOpportunity() {
        OpportunityRequest req = OpportunityRequest.builder()
                .name("DeleteTest")
                .probability(10)
                .expectedValue(BigDecimal.valueOf(500))
                .stage("Initial")
                .build();

        OpportunityResponse created = opportunityService.create(req);
        Long id = created.getId();

        opportunityService.delete(id);

        Assertions.assertThrows(IllegalArgumentException.class, () -> opportunityService.findById(id));
    }

    @Test
    void findAllOpportunities() {
        List<OpportunityResponse> all = opportunityService.findAll();
        assertThat(all).isNotNull();
    }
}
