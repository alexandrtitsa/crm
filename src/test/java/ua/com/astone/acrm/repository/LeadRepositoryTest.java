package ua.com.astone.acrm.repository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import ua.com.astone.acrm.model.Lead;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class LeadRepositoryTest {

    @Autowired
    private LeadRepository leadRepository;

    @Test
    @DisplayName("countByStatus: повертає правильну кількість за статусами")
    void countByStatus_returnsCorrectCounts() {
        // given
        leadRepository.save(new Lead(null, "web", "new", null, null));
        leadRepository.save(new Lead(null, "email", "new", null, null));
        leadRepository.save(new Lead(null, "phone", "contacted", null, null));

        // when
        var stats = leadRepository.countByStatus();

        // then
        assertThat(stats).anySatisfy(dto -> {
            if (dto.status().equals("new")) assertThat(dto.count()).isEqualTo(2L);
            if (dto.status().equals("contacted")) assertThat(dto.count()).isEqualTo(1L);
        });
    }

    @Test
    @DisplayName("findBySourceIgnoreCaseContainingOrStatusIgnoreCaseContaining: повертає за ключем")
    void findBySourceOrStatusIgnoreCaseContaining_works() {
        leadRepository.deleteAll();

        leadRepository.save(new Lead(null, "Web", "Contacted", null, null));
        leadRepository.save(new Lead(null, "Email", "New", null, null));
        leadRepository.save(new Lead(null, "Phone", "Qualified", null, null));

        var result = leadRepository.findBySourceIgnoreCaseContainingOrStatusIgnoreCaseContaining("web", "contact", org.springframework.data.domain.PageRequest.of(0, 10));

        assertThat(result.getTotalElements()).isEqualTo(1);
        assertThat(result.getContent().getFirst().getSource()).isEqualTo("Web");
    }
}
