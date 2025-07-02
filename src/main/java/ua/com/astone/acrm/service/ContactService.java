package ua.com.astone.acrm.service;

import ua.com.astone.acrm.dto.contact.*;
import org.springframework.data.domain.Page;
import java.util.List;

public interface ContactService {
    ContactResponse findById(Long id);
    List<ContactResponse> findAll();
    Page<ContactResponse> findAllPaged(ContactPageRequest request);
    ContactResponse create(ContactRequest request);
    ContactResponse update(Long id, ContactRequest request);
    void delete(Long id);
}
