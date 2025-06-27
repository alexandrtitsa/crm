package ua.com.astone.acrm.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ua.com.astone.acrm.dto.contact.ContactRequest;
import ua.com.astone.acrm.dto.contact.ContactResponse;
import ua.com.astone.acrm.model.Contact;

import java.util.List;

public interface ContactService {

    Page<ContactResponse> findAll(Pageable pageable);

    List<ContactResponse> findAll();

    ContactResponse findById(Long id);

    Contact findByIdEntity(Long id);

    ContactResponse create(ContactRequest request);

    ContactResponse update(Long id, ContactRequest request);

    void deleteById(Long id);

    void save(Contact contact);
}
