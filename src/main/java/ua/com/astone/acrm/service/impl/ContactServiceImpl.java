package ua.com.astone.acrm.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.com.astone.acrm.dto.contact.*;
import ua.com.astone.acrm.model.Contact;
import ua.com.astone.acrm.repository.ContactRepository;
import ua.com.astone.acrm.service.ContactService;
import ua.com.astone.acrm.util.ContactMapper;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ContactServiceImpl implements ContactService {

    private final ContactRepository contactRepository;
    private final ContactMapper contactMapper;

    @Override
    public ContactResponse findById(Long id) {
        return contactRepository.findById(id)
                .map(contactMapper::toResponse)
                .orElseThrow(() -> new IllegalArgumentException("Contact not found"));
    }

    @Override
    public List<ContactResponse> findAll() {
        return contactRepository.findAll().stream()
                .map(contactMapper::toResponse)
                .toList();
    }

    @Override
    public Page<ContactResponse> findAllPaged(ContactPageRequest request) {
        Pageable pageable = PageRequest.of(request.getPage(), request.getSize(), request.toSort());

        Page<Contact> page;
        if (request.getSearch() != null && !request.getSearch().isBlank()) {
            String q = request.getSearch().toLowerCase();
            page = contactRepository.findByFirstNameIgnoreCaseContainingOrLastNameIgnoreCaseContaining(q, q, pageable);
        } else {
            page = contactRepository.findAll(pageable);
        }

        return page.map(contactMapper::toResponse);
    }

    @Override
    public ContactResponse create(ContactRequest request) {
        Contact contact = contactMapper.toEntity(request);
        return contactMapper.toResponse(contactRepository.save(contact));
    }

    @Override
    public ContactResponse update(Long id, ContactRequest request) {
        Contact contact = contactRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Contact not found"));
        contactMapper.updateEntity(request, contact);
        return contactMapper.toResponse(contactRepository.save(contact));
    }

    @Override
    public void delete(Long id) {
        contactRepository.deleteById(id);
    }
}
