package ua.com.astone.acrm.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.com.astone.acrm.dto.opportunity.*;
import ua.com.astone.acrm.model.Opportunity;
import ua.com.astone.acrm.repository.OpportunityRepository;
import ua.com.astone.acrm.service.OpportunityService;
import ua.com.astone.acrm.util.OpportunityMapper;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class OpportunityServiceImpl implements OpportunityService {

    private final OpportunityRepository opportunityRepository;
    private final OpportunityMapper opportunityMapper;

    @Override
    public OpportunityResponse findById(Long id) {
        return opportunityRepository.findById(id)
                .map(opportunityMapper::toResponse)
                .orElseThrow(() -> new IllegalArgumentException("Opportunity not found"));
    }

    @Override
    public List<OpportunityResponse> findAll() {
        return opportunityRepository.findAll().stream()
                .map(opportunityMapper::toResponse)
                .toList();
    }

    @Override
    public Page<OpportunityResponse> findAllPaged(OpportunityPageRequest request) {
        Pageable pageable = PageRequest.of(request.getPage(), request.getSize(), request.toSort());

        Page<Opportunity> page;
        if (request.getSearch() != null && !request.getSearch().isBlank()) {
            String q = request.getSearch().toLowerCase();
            page = opportunityRepository.findByNameIgnoreCaseContainingOrStageIgnoreCaseContaining(q, q, pageable);
        } else {
            page = opportunityRepository.findAll(pageable);
        }

        return page.map(opportunityMapper::toResponse);
    }

    @Override
    public OpportunityResponse create(OpportunityRequest request) {
        Opportunity opportunity = opportunityMapper.toEntity(request);
        return opportunityMapper.toResponse(opportunityRepository.save(opportunity));
    }

    @Override
    public OpportunityResponse update(Long id, OpportunityRequest request) {
        Opportunity opportunity = opportunityRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Opportunity not found"));
        opportunityMapper.updateEntity(request, opportunity);
        return opportunityMapper.toResponse(opportunityRepository.save(opportunity));
    }

    @Override
    public void delete(Long id) {
        opportunityRepository.deleteById(id);
    }
}
