package ua.com.astone.acrm.util;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ua.com.astone.acrm.dto.lead.LeadRequest;
import ua.com.astone.acrm.dto.lead.LeadResponse;
import ua.com.astone.acrm.model.Company;
import ua.com.astone.acrm.model.Contact;
import ua.com.astone.acrm.model.Lead;
import ua.com.astone.acrm.repository.CompanyRepository;
import ua.com.astone.acrm.repository.ContactRepository;

@Component
@RequiredArgsConstructor
public class LeadMapper {

    private final CompanyRepository companyRepository;
    private final ContactRepository contactRepository;

    public Lead toEntity(LeadRequest request) {
        return Lead.builder()
                .source(request.getSource())
                .status(request.getStatus())
                .contact(resolveContact(request.getContactId()))
                .company(resolveCompany(request.getCompanyId()))
                .build();
    }

    public void updateEntity(LeadRequest request, Lead lead) {
        lead.setSource(request.getSource());
        lead.setStatus(request.getStatus());
        lead.setContact(resolveContact(request.getContactId()));
        lead.setCompany(resolveCompany(request.getCompanyId()));
    }

    public LeadResponse toResponse(Lead lead) {
        return LeadResponse.builder()
                .id(lead.getId())
                .source(lead.getSource())
                .status(lead.getStatus())
                .contactId(lead.getContact() != null ? lead.getContact().getId() : null)
                .contactFirstName(lead.getContact() != null ? lead.getContact().getFirstName() : null)
                .contactLastName(lead.getContact() != null ? lead.getContact().getLastName() : null)
                .companyId(lead.getCompany() != null ? lead.getCompany().getId() : null)
                .companyName(lead.getCompany() != null ? lead.getCompany().getName() : null)
                .build();
    }

    private Contact resolveContact(Long id) {
        return id != null ? contactRepository.findById(id).orElse(null) : null;
    }

    private Company resolveCompany(Long id) {
        return id != null ? companyRepository.findById(id).orElse(null) : null;
    }
}
