package ua.com.astone.acrm.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import ua.com.astone.acrm.model.Company;
import ua.com.astone.acrm.model.Contact;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class ContactRepositoryTest {

    @Autowired
    private ContactRepository contactRepository;

    @Autowired
    private CompanyRepository companyRepository;

    @Test
    void findByFirstNameIgnoreCaseContainingOrLastNameIgnoreCaseContaining_works() {
        contactRepository.deleteAll();
        companyRepository.deleteAll();

        var company = companyRepository.save(Company.builder().name("Test Inc").build());

        contactRepository.save(Contact.builder()
                .firstName("Ivan")
                .lastName("Petrov")
                .email("test1@example.com")
                .company(company)
                .build());
        contactRepository.save(Contact.builder()
                .firstName("Anna")
                .lastName("Shevchenko")
                .email("test2@example.com")
                .company(company)
                .build());

        var result = contactRepository.findByFirstNameIgnoreCaseContainingOrLastNameIgnoreCaseContaining(
                "ivan", "shev", org.springframework.data.domain.PageRequest.of(0, 10));

        assertThat(result.getTotalElements()).isEqualTo(2);
        assertThat(result.getContent()).extracting("firstName").containsExactlyInAnyOrder("Ivan", "Anna");
    }
}
