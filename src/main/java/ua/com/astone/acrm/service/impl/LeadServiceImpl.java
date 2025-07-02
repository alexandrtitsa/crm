package ua.com.astone.acrm.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.com.astone.acrm.dto.lead.*;
import ua.com.astone.acrm.model.Lead;
import ua.com.astone.acrm.repository.LeadRepository;
import ua.com.astone.acrm.service.LeadService;
import ua.com.astone.acrm.util.LeadMapper;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class LeadServiceImpl implements LeadService {

    private final LeadRepository leadRepository;
    private final LeadMapper leadMapper;

    @Override
    public LeadResponse findById(Long id) {
        return leadRepository.findById(id)
                .map(leadMapper::toResponse)
                .orElseThrow(() -> new IllegalArgumentException("Lead not found"));
    }

    @Override
    public List<LeadResponse> findAll() {
        return leadRepository.findAll().stream()
                .map(leadMapper::toResponse)
                .toList();
    }

    @Override
    public Page<LeadResponse> findAllPaged(LeadPageRequest request) {
        Pageable pageable = PageRequest.of(request.getPage(), request.getSize(), request.toSort());

        Page<Lead> page;
        if (request.getSearch() != null && !request.getSearch().isBlank()) {
            String q = request.getSearch().toLowerCase();
            // Приклад пошуку за source або status (додай інше якщо потрібно)
            page = leadRepository.findBySourceIgnoreCaseContainingOrStatusIgnoreCaseContaining(q, q, pageable);
        } else {
            page = leadRepository.findAll(pageable);
        }

        return page.map(leadMapper::toResponse);
    }

    @Override
    public LeadResponse create(LeadRequest request) {
        Lead lead = leadMapper.toEntity(request);
        return leadMapper.toResponse(leadRepository.save(lead));
    }

    @Override
    public LeadResponse update(Long id, LeadRequest request) {
        Lead lead = leadRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Lead not found"));
        leadMapper.updateEntity(request, lead);
        return leadMapper.toResponse(leadRepository.save(lead));
    }

    @Override
    public void delete(Long id) {
        leadRepository.deleteById(id);
    }
}
