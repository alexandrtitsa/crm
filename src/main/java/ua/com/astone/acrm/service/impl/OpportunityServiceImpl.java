package ua.com.astone.acrm.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.com.astone.acrm.dto.opportunity.OpportunityRequest;
import ua.com.astone.acrm.dto.opportunity.OpportunityResponse;
import ua.com.astone.acrm.exception.NotFoundException;
import ua.com.astone.acrm.model.Opportunity;
import ua.com.astone.acrm.repository.OpportunityRepository;
import ua.com.astone.acrm.service.OpportunityService;
import ua.com.astone.acrm.util.OpportunityMapper;

import org.springframework.data.domain.Pageable;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class OpportunityServiceImpl implements OpportunityService {

    private final OpportunityRepository repository;
    private final OpportunityMapper mapper;

    @Override
    public List<OpportunityResponse> findAll() {
        return repository.findAll().stream().map(mapper::toResponse).toList();
    }

    @Override
    public Page<OpportunityResponse> findAll(Pageable pageable) {
        return repository.findAll(pageable)
                .map(mapper::toResponse);
    }

    @Override
    public OpportunityResponse findById(Long id) {
        return mapper.toResponse(findByIdOrThrow(id));
    }

    @Override
    @Transactional
    public OpportunityResponse create(OpportunityRequest request) {
        return mapper.toResponse(repository.save(mapper.toEntity(request)));
    }

    @Override
    @Transactional
    public OpportunityResponse update(Long id, OpportunityRequest request) {
        Opportunity opportunity = findByIdOrThrow(id);
        mapper.updateEntity(request, opportunity);
        return mapper.toResponse(repository.save(opportunity));
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        repository.deleteById(id);
    }

    private Opportunity findByIdOrThrow(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new NotFoundException("Opportunity", id));
    }
}
