package ua.com.astone.acrm.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ua.com.astone.acrm.dto.lead.LeadRequest;
import ua.com.astone.acrm.dto.lead.LeadResponse;

import java.util.List;

public interface LeadService {

    Page<LeadResponse> findAll(Pageable pageable);

    List<LeadResponse> findAll();

    LeadResponse findById(Long id);

    LeadResponse create(LeadRequest request);

    LeadResponse update(Long id, LeadRequest request);

    void deleteById(Long id);
}
