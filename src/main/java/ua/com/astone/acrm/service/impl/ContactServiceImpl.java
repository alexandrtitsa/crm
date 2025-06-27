package ua.com.astone.acrm.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.com.astone.acrm.dto.contact.ContactRequest;
import ua.com.astone.acrm.dto.contact.ContactResponse;
import ua.com.astone.acrm.exception.NotFoundException;
import ua.com.astone.acrm.model.Contact;
import ua.com.astone.acrm.repository.ContactRepository;
import ua.com.astone.acrm.service.ContactService;
import ua.com.astone.acrm.util.ContactMapper;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ContactServiceImpl implements ContactService {

    private final ContactRepository contactRepository;
    private final ContactMapper contactMapper;

    @Override
    public Page<ContactResponse> findAll(Pageable pageable) {
        return contactRepository.findAll(pageable)
                .map(contactMapper::toResponse);
    }

    @Override
    public List<ContactResponse> findAll() {
        return contactRepository.findAll().stream()
                .map(contactMapper::toResponse)
                .toList();
    }

    @Override
    public ContactResponse findById(Long id) {
        return contactMapper.toResponse(findByIdOrThrow(id));
    }

    @Override
    public Contact findByIdEntity(Long id) {
        return findByIdOrThrow(id);
    }

    @Override
    @Transactional
    public ContactResponse create(ContactRequest request) {
        Contact contact = contactMapper.toEntity(request);
        return contactMapper.toResponse(contactRepository.save(contact));
    }

    @Override
    @Transactional
    public ContactResponse update(Long id, ContactRequest request) {
        Contact existing = findByIdOrThrow(id);
        contactMapper.updateEntity(request, existing);
        return contactMapper.toResponse(contactRepository.save(existing));
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        if (!contactRepository.existsById(id)) {
            throw new NotFoundException("Contact", id);
        }
        contactRepository.deleteById(id);
    }

    @Override
    @Transactional
    public void save(Contact contact) {
        contactRepository.save(contact);
    }

    private Contact findByIdOrThrow(Long id) {
        return contactRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Contact", id));
    }
}
