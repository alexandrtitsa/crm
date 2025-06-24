package ua.com.astone.acrm.service;

import ua.com.astone.acrm.dto.contact.*;

import java.util.List;

public interface ContactService {
    ContactResponse create(ContactRequest request);
    ContactResponse findById(Long id);
    List<ContactResponse> findAll();
    ContactResponse update(Long id, ContactRequest request);
    void delete(Long id);
}
