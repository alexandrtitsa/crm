package ua.com.astone.acrm.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.com.astone.acrm.dto.company.*;
import ua.com.astone.acrm.model.Company;
import ua.com.astone.acrm.repository.CompanyRepository;
import ua.com.astone.acrm.service.CompanyService;
import ua.com.astone.acrm.util.CompanyMapper;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class CompanyServiceImpl implements CompanyService {

    private final CompanyRepository companyRepository;
    private final CompanyMapper companyMapper;

    @Override
    public CompanyResponse findById(Long id) {
        return companyRepository.findById(id)
                .map(companyMapper::toResponse)
                .orElseThrow(() -> new IllegalArgumentException("Company not found"));
    }

    @Override
    public List<CompanyResponse> findAll() {
        return companyRepository.findAll().stream()
                .map(companyMapper::toResponse)
                .toList();
    }

    @Override
    public Page<CompanyResponse> findAllPaged(CompanyPageRequest request) {
        Pageable pageable = PageRequest.of(request.getPage(), request.getSize(), request.toSort());

        Page<Company> page;
        if (request.getSearch() != null && !request.getSearch().isBlank()) {
            String q = request.getSearch().toLowerCase();
            page = companyRepository.findByNameIgnoreCaseContainingOrIndustryIgnoreCaseContaining(q, q, pageable);
        } else {
            page = companyRepository.findAll(pageable);
        }

        return page.map(companyMapper::toResponse);
    }

    @Override
    public CompanyResponse create(CompanyRequest request) {
        Company company = companyMapper.toEntity(request);
        return companyMapper.toResponse(companyRepository.save(company));
    }

    @Override
    public CompanyResponse update(Long id, CompanyRequest request) {
        Company company = companyRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Company not found"));
        companyMapper.updateEntity(request, company);
        return companyMapper.toResponse(companyRepository.save(company));
    }

    @Override
    public void delete(Long id) {
        companyRepository.deleteById(id);
    }
}
