package ua.com.astone.acrm.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import ua.com.astone.acrm.model.Contact;

public interface ContactRepository extends JpaRepository<Contact, Long> {
    Page<Contact> findByFirstNameIgnoreCaseContainingOrLastNameIgnoreCaseContaining(String q, String q1, Pageable pageable);
}
