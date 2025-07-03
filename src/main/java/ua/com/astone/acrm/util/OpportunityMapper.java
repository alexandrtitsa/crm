package ua.com.astone.acrm.util;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ua.com.astone.acrm.dto.opportunity.OpportunityRequest;
import ua.com.astone.acrm.dto.opportunity.OpportunityResponse;
import ua.com.astone.acrm.model.Lead;
import ua.com.astone.acrm.model.Opportunity;
import ua.com.astone.acrm.repository.LeadRepository;

@Component
@RequiredArgsConstructor
public class OpportunityMapper {

    private final LeadRepository leadRepository;

    public Opportunity toEntity(OpportunityRequest request) {
        return Opportunity.builder()
                .name(request.getName())
                .probability(request.getProbability())
                .expectedValue(request.getExpectedValue())
                .stage(request.getStage())
                .lead(resolveLead(request.getLeadId()))
                .build();
    }

    public void updateEntity(OpportunityRequest request, Opportunity opportunity) {
        opportunity.setName(request.getName());
        opportunity.setProbability(request.getProbability());
        opportunity.setExpectedValue(request.getExpectedValue());
        opportunity.setStage(request.getStage());
        opportunity.setLead(resolveLead(request.getLeadId()));
    }

    public OpportunityResponse toResponse(Opportunity opportunity) {
        return OpportunityResponse.builder()
                .id(opportunity.getId())
                .name(opportunity.getName())
                .probability(opportunity.getProbability())
                .expectedValue(opportunity.getExpectedValue())
                .stage(opportunity.getStage())
                .leadId(opportunity.getLead() != null ? opportunity.getLead().getId() : null)
                .leadStatus(opportunity.getLead() != null ? opportunity.getLead().getStatus() : null)
                .build();
    }

    private Lead resolveLead(Long id) {
        return id != null ? leadRepository.findById(id).orElse(null) : null;
    }
}
