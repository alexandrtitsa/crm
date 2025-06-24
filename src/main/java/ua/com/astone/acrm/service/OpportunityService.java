package ua.com.astone.acrm.service;

import ua.com.astone.acrm.dto.opportunity.*;

import java.util.List;

public interface OpportunityService {
    OpportunityResponse create(OpportunityRequest request);
    OpportunityResponse findById(Long id);
    List<OpportunityResponse> findAll();
    OpportunityResponse update(Long id, OpportunityRequest request);
    void delete(Long id);
}
