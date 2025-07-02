package ua.com.astone.acrm.service;

import ua.com.astone.acrm.dto.lead.*;
import org.springframework.data.domain.Page;
import java.util.List;

public interface LeadService {
    LeadResponse findById(Long id);
    List<LeadResponse> findAll();
    Page<LeadResponse> findAllPaged(LeadPageRequest request);
    LeadResponse create(LeadRequest request);
    LeadResponse update(Long id, LeadRequest request);
    void delete(Long id);
}
