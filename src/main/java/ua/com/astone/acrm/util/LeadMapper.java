package ua.com.astone.acrm.util;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ua.com.astone.acrm.dto.lead.LeadRequest;
import ua.com.astone.acrm.dto.lead.LeadResponse;
import ua.com.astone.acrm.model.Lead;
import ua.com.astone.acrm.repository.CompanyRepository;
import ua.com.astone.acrm.repository.ContactRepository;

@Component
@RequiredArgsConstructor
public class LeadMapper {

    private final CompanyRepository companyRepository;
    private final ContactRepository contactRepository;

    public Lead toEntity(LeadRequest request) {
        Lead lead = new Lead();
        lead.setSource(request.getSource());
        lead.setStatus(request.getStatus());

        if (request.getCompanyId() != null) {
            lead.setCompany(companyRepository.findById(request.getCompanyId()).orElse(null));
        }

        if (request.getContactId() != null) {
            lead.setContact(contactRepository.findById(request.getContactId()).orElse(null));
        }

        return lead;
    }

    public void updateEntity(LeadRequest request, Lead lead) {
        lead.setSource(request.getSource());
        lead.setStatus(request.getStatus());

        if (request.getCompanyId() != null) {
            lead.setCompany(companyRepository.findById(request.getCompanyId()).orElse(null));
        } else {
            lead.setCompany(null);
        }

        if (request.getContactId() != null) {
            lead.setContact(contactRepository.findById(request.getContactId()).orElse(null));
        } else {
            lead.setContact(null);
        }
    }

    public LeadResponse toResponse(Lead lead) {
        return LeadResponse.builder()
                .id(lead.getId())
                .source(lead.getSource())
                .status(lead.getStatus())
                .contactId(lead.getContact() != null ? lead.getContact().getId() : null)
                .contactName(lead.getContact() != null ? lead.getContact().getFirstName() + " " + lead.getContact().getLastName() : null)
                .companyId(lead.getCompany() != null ? lead.getCompany().getId() : null)
                .companyName(lead.getCompany() != null ? lead.getCompany().getName() : null)
                .build();
    }
}
