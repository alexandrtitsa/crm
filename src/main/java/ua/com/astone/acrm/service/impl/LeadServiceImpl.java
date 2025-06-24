package ua.com.astone.acrm.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.com.astone.acrm.dto.lead.*;
import ua.com.astone.acrm.model.Company;
import ua.com.astone.acrm.model.Contact;
import ua.com.astone.acrm.model.Lead;
import ua.com.astone.acrm.repository.CompanyRepository;
import ua.com.astone.acrm.repository.ContactRepository;
import ua.com.astone.acrm.repository.LeadRepository;
import ua.com.astone.acrm.service.LeadService;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class LeadServiceImpl implements LeadService {

    private final LeadRepository leadRepository;
    private final ContactRepository contactRepository;
    private final CompanyRepository companyRepository;

    @Override
    public LeadResponse create(LeadRequest request) {
        Lead lead = toEntity(request);
        return toDto(leadRepository.save(lead));
    }

    @Override
    public LeadResponse findById(Long id) {
        return toDto(leadRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Lead not found")));
    }

    @Override
    public List<LeadResponse> findAll() {
        return leadRepository.findAll().stream()
                .map(this::toDto)
                .toList();
    }

    @Override
    public LeadResponse update(Long id, LeadRequest request) {
        Lead lead = leadRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Lead not found"));

        lead.setSource(request.getSource());
        lead.setStatus(request.getStatus());
        lead.setContact(getContactById(request.getContactId()));
        lead.setCompany(getCompanyById(request.getCompanyId()));

        return toDto(leadRepository.save(lead));
    }

    @Override
    public void delete(Long id) {
        leadRepository.deleteById(id);
    }

    private Lead toEntity(LeadRequest req) {
        return Lead.builder()
                .source(req.getSource())
                .status(req.getStatus())
                .contact(getContactById(req.getContactId()))
                .company(getCompanyById(req.getCompanyId()))
                .build();
    }

    private LeadResponse toDto(Lead lead) {
        return LeadResponse.builder()
                .id(lead.getId())
                .source(lead.getSource())
                .status(lead.getStatus())
                .contactId(lead.getContact() != null ? lead.getContact().getId() : null)
                .contactName(lead.getContact() != null ?
                        lead.getContact().getFirstName() + " " + lead.getContact().getLastName() : null)
                .companyId(lead.getCompany() != null ? lead.getCompany().getId() : null)
                .companyName(lead.getCompany() != null ? lead.getCompany().getName() : null)
                .build();
    }

    private Contact getContactById(Long id) {
        if (id == null) return null;
        return contactRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Contact not found"));
    }

    private Company getCompanyById(Long id) {
        if (id == null) return null;
        return companyRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Company not found"));
    }
}
