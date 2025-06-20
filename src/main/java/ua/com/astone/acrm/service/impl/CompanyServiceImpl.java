package ua.com.astone.acrm.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.com.astone.acrm.dto.company.*;
import ua.com.astone.acrm.model.Company;
import ua.com.astone.acrm.repository.CompanyRepository;
import ua.com.astone.acrm.service.CompanyService;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class CompanyServiceImpl implements CompanyService {

    private final CompanyRepository companyRepository;

    @Override
    public CompanyResponse create(CompanyRequest request) {
        Company company = Company.builder()
                .name(request.getName())
                .industry(request.getIndustry())
                .address(request.getAddress())
                .website(request.getWebsite())
                .description(request.getDescription())
                .build();
        return toDto(companyRepository.save(company));
    }

    @Override
    public CompanyResponse findById(Long id) {
        return toDto(companyRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Company not found")));
    }

    @Override
    public List<CompanyResponse> findAll() {
        return companyRepository.findAll().stream()
                .map(this::toDto)
                .toList();
    }

    @Override
    public Page<CompanyResponse> findAllPaged(CompanyPageRequest request) {
        Pageable pageable = PageRequest.of(request.getPage(), request.getSize(), request.getSortOrder());

        Page<Company> page;

        if (request.getSearch() != null && !request.getSearch().isBlank()) {
            String q = request.getSearch().toLowerCase();
            page = companyRepository.findByNameIgnoreCaseContainingOrIndustryIgnoreCaseContaining(q, q, pageable);
        } else {
            page = companyRepository.findAll(pageable);
        }

        return page.map(this::toDto);
    }

    @Override
    public CompanyResponse update(Long id, CompanyRequest request) {
        Company company = companyRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Company not found"));
        company.setName(request.getName());
        company.setIndustry(request.getIndustry());
        company.setAddress(request.getAddress());
        company.setWebsite(request.getWebsite());
        company.setDescription(request.getDescription());
        return toDto(companyRepository.save(company));
    }

    @Override
    public void delete(Long id) {
        companyRepository.deleteById(id);
    }

    private CompanyResponse toDto(Company company) {
        return CompanyResponse.builder()
                .id(company.getId())
                .name(company.getName())
                .industry(company.getIndustry())
                .address(company.getAddress())
                .website(company.getWebsite())
                .description(company.getDescription())
                .build();
    }
}
