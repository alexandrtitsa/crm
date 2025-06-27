package ua.com.astone.acrm.util;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ua.com.astone.acrm.dto.contact.ContactRequest;
import ua.com.astone.acrm.dto.contact.ContactResponse;
import ua.com.astone.acrm.model.Company;
import ua.com.astone.acrm.model.Contact;
import ua.com.astone.acrm.repository.CompanyRepository;

@Component
@RequiredArgsConstructor
public class ContactMapper {

    private final CompanyRepository companyRepository;

    public Contact toEntity(ContactRequest request) {
        Company company = companyRepository.findById(request.getCompanyId())
                .orElseThrow(() -> new IllegalArgumentException("Company not found with ID: " + request.getCompanyId()));

        return Contact.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .phone(request.getPhone())
                .position(request.getPosition())
                .company(company)
                .build();
    }

    public ContactResponse toResponse(Contact contact) {
        return ContactResponse.builder()
                .id(contact.getId())
                .firstName(contact.getFirstName())
                .lastName(contact.getLastName())
                .email(contact.getEmail())
                .phone(contact.getPhone())
                .position(contact.getPosition())
                .companyId(contact.getCompany() != null ? contact.getCompany().getId() : null)
                .companyName(contact.getCompany() != null ? contact.getCompany().getName() : null) // ✅ Додай
                .build();
    }

    public void updateEntity(ContactRequest request, Contact contact) {
        contact.setFirstName(request.getFirstName());
        contact.setLastName(request.getLastName());
        contact.setEmail(request.getEmail());
        contact.setPhone(request.getPhone());
        contact.setPosition(request.getPosition());

        Company company = companyRepository.findById(request.getCompanyId())
                .orElseThrow(() -> new IllegalArgumentException("Company not found: " + request.getCompanyId()));
        contact.setCompany(company);
    }
}
