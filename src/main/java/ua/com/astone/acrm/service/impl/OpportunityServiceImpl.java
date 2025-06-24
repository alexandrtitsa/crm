package ua.com.astone.acrm.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.com.astone.acrm.dto.opportunity.*;
import ua.com.astone.acrm.model.Lead;
import ua.com.astone.acrm.model.Opportunity;
import ua.com.astone.acrm.repository.LeadRepository;
import ua.com.astone.acrm.repository.OpportunityRepository;
import ua.com.astone.acrm.service.OpportunityService;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class OpportunityServiceImpl implements OpportunityService {

    private final OpportunityRepository opportunityRepository;
    private final LeadRepository leadRepository;

    @Override
    public OpportunityResponse create(OpportunityRequest request) {
        Opportunity opportunity = toEntity(request);
        return toDto(opportunityRepository.save(opportunity));
    }

    @Override
    public OpportunityResponse findById(Long id) {
        return toDto(opportunityRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Opportunity not found")));
    }

    @Override
    public List<OpportunityResponse> findAll() {
        return opportunityRepository.findAll().stream()
                .map(this::toDto)
                .toList();
    }

    @Override
    public OpportunityResponse update(Long id, OpportunityRequest request) {
        Opportunity opportunity = opportunityRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Opportunity not found"));

        opportunity.setName(request.getName());
        opportunity.setProbability(request.getProbability());
        opportunity.setExpectedValue(request.getExpectedValue());
        opportunity.setStage(request.getStage());
        opportunity.setLead(getLeadById(request.getLeadId()));

        return toDto(opportunityRepository.save(opportunity));
    }

    @Override
    public void delete(Long id) {
        opportunityRepository.deleteById(id);
    }

    private Opportunity toEntity(OpportunityRequest req) {
        return Opportunity.builder()
                .name(req.getName())
                .probability(req.getProbability())
                .expectedValue(req.getExpectedValue())
                .stage(req.getStage())
                .lead(getLeadById(req.getLeadId()))
                .build();
    }

    private Lead getLeadById(Long id) {
        return leadRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Lead not found"));
    }

    private OpportunityResponse toDto(Opportunity o) {
        return OpportunityResponse.builder()
                .id(o.getId())
                .name(o.getName())
                .probability(o.getProbability())
                .expectedValue(o.getExpectedValue())
                .stage(o.getStage())
                .leadId(o.getLead().getId())
                .leadSummary(o.getLead().getContact() != null
                        ? o.getLead().getContact().getFirstName() + " " + o.getLead().getContact().getLastName()
                        : o.getLead().getCompany() != null
                        ? o.getLead().getCompany().getName()
                        : "Без джерела")
                .build();
    }
}
