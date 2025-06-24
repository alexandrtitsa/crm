package ua.com.astone.acrm.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.com.astone.acrm.dto.contact.*;
import ua.com.astone.acrm.model.Company;
import ua.com.astone.acrm.model.Contact;
import ua.com.astone.acrm.repository.CompanyRepository;
import ua.com.astone.acrm.repository.ContactRepository;
import ua.com.astone.acrm.service.ContactService;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ContactServiceImpl implements ContactService {

    private final ContactRepository contactRepository;
    private final CompanyRepository companyRepository;

    @Override
    public ContactResponse create(ContactRequest request) {
        Contact contact = toEntity(request);
        return toDto(contactRepository.save(contact));
    }

    @Override
    public ContactResponse findById(Long id) {
        return toDto(contactRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Contact not found")));
    }

    @Override
    public List<ContactResponse> findAll() {
        return contactRepository.findAll().stream()
                .map(this::toDto)
                .toList();
    }

    @Override
    public ContactResponse update(Long id, ContactRequest request) {
        Contact contact = contactRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Contact not found"));

        contact.setFirstName(request.getFirstName());
        contact.setLastName(request.getLastName());
        contact.setEmail(request.getEmail());
        contact.setPhone(request.getPhone());
        contact.setPosition(request.getPosition());
        contact.setCompany(getCompanyById(request.getCompanyId()));

        return toDto(contactRepository.save(contact));
    }

    @Override
    public void delete(Long id) {
        contactRepository.deleteById(id);
    }

    private Contact toEntity(ContactRequest req) {
        return Contact.builder()
                .firstName(req.getFirstName())
                .lastName(req.getLastName())
                .email(req.getEmail())
                .phone(req.getPhone())
                .position(req.getPosition())
                .company(getCompanyById(req.getCompanyId()))
                .build();
    }

    private Company getCompanyById(Long id) {
        if (id == null) return null;
        return companyRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Company not found"));
    }

    private ContactResponse toDto(Contact c) {
        return ContactResponse.builder()
                .id(c.getId())
                .firstName(c.getFirstName())
                .lastName(c.getLastName())
                .email(c.getEmail())
                .phone(c.getPhone())
                .position(c.getPosition())
                .companyId(c.getCompany() != null ? c.getCompany().getId() : null)
                .companyName(c.getCompany() != null ? c.getCompany().getName() : null)
                .build();
    }
}
