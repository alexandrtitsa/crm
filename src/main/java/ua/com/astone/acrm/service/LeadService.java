package ua.com.astone.acrm.service;

import ua.com.astone.acrm.dto.lead.*;

import java.util.List;

public interface LeadService {
    LeadResponse create(LeadRequest request);
    LeadResponse findById(Long id);
    List<LeadResponse> findAll();
    LeadResponse update(Long id, LeadRequest request);
    void delete(Long id);
}
