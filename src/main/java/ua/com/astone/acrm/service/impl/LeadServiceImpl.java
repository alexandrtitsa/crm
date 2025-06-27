package ua.com.astone.acrm.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.com.astone.acrm.dto.lead.LeadRequest;
import ua.com.astone.acrm.dto.lead.LeadResponse;
import ua.com.astone.acrm.exception.NotFoundException;
import ua.com.astone.acrm.model.Lead;
import ua.com.astone.acrm.repository.LeadRepository;
import ua.com.astone.acrm.util.LeadMapper;
import ua.com.astone.acrm.service.LeadService;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class LeadServiceImpl implements LeadService {

    private final LeadRepository leadRepository;
    private final LeadMapper leadMapper;

    @Override
    public Page<LeadResponse> findAll(Pageable pageable) {
        return leadRepository.findAll(pageable)
                .map(leadMapper::toResponse);
    }

    @Override
    public List<LeadResponse> findAll() {
        return leadRepository.findAll()
                .stream()
                .map(leadMapper::toResponse)
                .toList();
    }

    @Override
    public LeadResponse findById(Long id) {
        return leadMapper.toResponse(findByIdOrThrow(id));
    }

    @Override
    @Transactional
    public LeadResponse create(LeadRequest request) {
        Lead lead = leadMapper.toEntity(request);
        return leadMapper.toResponse(leadRepository.save(lead));
    }

    @Override
    @Transactional
    public LeadResponse update(Long id, LeadRequest request) {
        Lead existing = findByIdOrThrow(id);
        leadMapper.updateEntity(request, existing);
        return leadMapper.toResponse(leadRepository.save(existing));
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        if (!leadRepository.existsById(id)) {
            throw new NotFoundException("Lead", id);
        }
        leadRepository.deleteById(id);
    }

    private Lead findByIdOrThrow(Long id) {
        return leadRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Lead", id));
    }
}
