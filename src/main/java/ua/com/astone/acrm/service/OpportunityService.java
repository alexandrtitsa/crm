package ua.com.astone.acrm.service;

import ua.com.astone.acrm.dto.opportunity.*;
import org.springframework.data.domain.Page;
import java.util.List;

public interface OpportunityService {
    OpportunityResponse findById(Long id);
    List<OpportunityResponse> findAll();
    Page<OpportunityResponse> findAllPaged(OpportunityPageRequest request);
    OpportunityResponse create(OpportunityRequest request);
    OpportunityResponse update(Long id, OpportunityRequest request);
    void delete(Long id);
}
