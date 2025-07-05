package ua.com.astone.acrm.service;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.containers.PostgreSQLContainer;
import ua.com.astone.acrm.dto.lead.LeadRequest;
import ua.com.astone.acrm.dto.lead.LeadResponse;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@Testcontainers
class LeadServiceIntegrationTest {

    @Container
    static final PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16.3")
            .withDatabaseName("testdb")
            .withUsername("testuser")
            .withPassword("testpass");

    @Autowired
    private LeadService leadService;

    @Test
    void createAndFindLead() {
        LeadRequest req = LeadRequest.builder()
                .source("web")
                .status("new")
                .contactId(null)
                .companyId(null)
                .build();

        LeadResponse created = leadService.create(req);

        assertThat(created.getId()).isNotNull();
        assertThat(created.getSource()).isEqualTo("web");
        assertThat(created.getStatus()).isEqualTo("new");

        LeadResponse found = leadService.findById(created.getId());
        assertThat(found.getSource()).isEqualTo("web");
        assertThat(found.getStatus()).isEqualTo("new");
    }

    @Test
    void updateLead() {
        LeadRequest req = LeadRequest.builder()
                .source("landing")
                .status("initial")
                .contactId(null)
                .companyId(null)
                .build();

        LeadResponse created = leadService.create(req);

        LeadRequest updateReq = LeadRequest.builder()
                .source("updated")
                .status("converted")
                .contactId(null)
                .companyId(null)
                .build();

        LeadResponse updated = leadService.update(created.getId(), updateReq);

        assertThat(updated.getSource()).isEqualTo("updated");
        assertThat(updated.getStatus()).isEqualTo("converted");
    }

    @Test
    void deleteLead() {
        LeadRequest req = LeadRequest.builder()
                .source("delete")
                .status("archived")
                .contactId(null)
                .companyId(null)
                .build();

        LeadResponse created = leadService.create(req);
        Long id = created.getId();

        leadService.delete(id);

        assertThrows(IllegalArgumentException.class, () -> leadService.findById(id));
    }

    @Test
    void findAllLeads() {
        List<LeadResponse> all = leadService.findAll();
        assertThat(all).isNotNull();
    }
}
