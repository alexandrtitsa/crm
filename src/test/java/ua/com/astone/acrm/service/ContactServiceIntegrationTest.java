package ua.com.astone.acrm.service;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.containers.PostgreSQLContainer;
import ua.com.astone.acrm.dto.contact.ContactRequest;
import ua.com.astone.acrm.dto.contact.ContactResponse;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@Testcontainers
class ContactServiceIntegrationTest {

    @Container
    static final PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16.3")
            .withDatabaseName("testdb")
            .withUsername("testuser")
            .withPassword("testpass");

    @Autowired
    private ContactService contactService;

    @Test
    void createAndFindContact() {
        ContactRequest req = ContactRequest.builder()
                .firstName("Іван")
                .lastName("Петров")
                .email("ivan.petrov@example.com")
                .phone("+380991234567")
                .position("Manager")
                .companyId(null)
                .build();

        ContactResponse created = contactService.create(req);

        assertThat(created.getId()).isNotNull();
        assertThat(created.getFirstName()).isEqualTo("Іван");
        assertThat(created.getLastName()).isEqualTo("Петров");
        assertThat(created.getEmail()).isEqualTo("ivan.petrov@example.com");

    }

    @Test
    void updateContact() {
        ContactRequest req = ContactRequest.builder()
                .firstName("Анна")
                .lastName("Сидорова")
                .email("anna.sydorova@example.com")
                .phone("+380987654321")
                .position("Sales")
                .companyId(null)
                .build();

        ContactResponse created = contactService.create(req);

        ContactRequest updateReq = ContactRequest.builder()
                .firstName("Анна")
                .lastName("Сидоренко")
                .email("anna.sydorenko@example.com")
                .phone("+380987654321")
                .position("Sales Lead")
                .companyId(null)
                .build();

        ContactResponse updated = contactService.update(created.getId(), updateReq);

        assertThat(updated.getFirstName()).isEqualTo("Анна");
        assertThat(updated.getLastName()).isEqualTo("Сидоренко");
        assertThat(updated.getEmail()).isEqualTo("anna.sydorenko@example.com");
        assertThat(updated.getPosition()).isEqualTo("Sales Lead");
    }

    @Test
    void deleteContact() {
        ContactRequest req = ContactRequest.builder()
                .firstName("Олег")
                .lastName("Тестовий")
                .email("oleg.test@example.com")
                .phone("+380999998877")
                .position("Tester")
                .companyId(null)
                .build();

        ContactResponse created = contactService.create(req);
        Long id = created.getId();

        contactService.delete(id);

        assertThrows(IllegalArgumentException.class, () -> contactService.findById(id));
    }

    @Test
    void findAllContacts() {
        List<ContactResponse> all = contactService.findAll();
        assertThat(all).isNotNull();
    }
}
