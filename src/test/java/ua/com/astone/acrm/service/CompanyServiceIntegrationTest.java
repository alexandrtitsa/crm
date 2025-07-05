package ua.com.astone.acrm.service;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.containers.PostgreSQLContainer;
import ua.com.astone.acrm.dto.company.CompanyRequest;
import ua.com.astone.acrm.dto.company.CompanyResponse;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@Testcontainers
class CompanyServiceIntegrationTest {

    @Container
    static final PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16.3")
            .withDatabaseName("testdb")
            .withUsername("testuser")
            .withPassword("testpass");

    @Autowired
    private CompanyService companyService;

    @Test
    void createAndFindCompany() {
        CompanyRequest req = CompanyRequest.builder()
                .name("Test Company")
                .industry("IT")
                .address("Kyiv, Ukraine")
                .website("https://test-company.com")
                .description("Опис компанії")
                .build();

        CompanyResponse created = companyService.create(req);

        assertThat(created.getId()).isNotNull();
        assertThat(created.getName()).isEqualTo("Test Company");

        CompanyResponse found = companyService.findById(created.getId());
        assertThat(found.getIndustry()).isEqualTo("IT");
        assertThat(found.getAddress()).contains("Kyiv");
    }

    @Test
    void updateCompany() {
        CompanyRequest req = CompanyRequest.builder()
                .name("Old Name")
                .industry("Retail")
                .address("Lviv")
                .website("http://retail.com")
                .description("Old desc")
                .build();

        CompanyResponse created = companyService.create(req);

        CompanyRequest updateReq = CompanyRequest.builder()
                .name("New Name")
                .industry("E-commerce")
                .address("Dnipro")
                .website("http://new.com")
                .description("New desc")
                .build();

        CompanyResponse updated = companyService.update(created.getId(), updateReq);

        assertThat(updated.getName()).isEqualTo("New Name");
        assertThat(updated.getIndustry()).isEqualTo("E-commerce");
        assertThat(updated.getAddress()).isEqualTo("Dnipro");
    }

    @Test
    void deleteCompany() {
        CompanyRequest req = CompanyRequest.builder()
                .name("To Delete")
                .industry("Consulting")
                .address("Kharkiv")
                .website(null)
                .description(null)
                .build();

        CompanyResponse created = companyService.create(req);
        Long id = created.getId();

        companyService.delete(id);

        assertThrows(IllegalArgumentException.class, () -> companyService.findById(id));
    }

    @Test
    void findAllCompanies() {
        List<CompanyResponse> all = companyService.findAll();
        assertThat(all).isNotNull();
    }
}
