package ua.com.astone.acrm.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ua.com.astone.acrm.dto.opportunity.OpportunityRequest;
import ua.com.astone.acrm.dto.opportunity.OpportunityResponse;

import java.util.List;

public interface OpportunityService {

    List<OpportunityResponse> findAll();

    Page<OpportunityResponse> findAll(Pageable pageable);

    OpportunityResponse findById(Long id);

    OpportunityResponse create(OpportunityRequest request);

    OpportunityResponse update(Long id, OpportunityRequest request);

    void deleteById(Long id);
}
