package ua.com.astone.acrm.util;

import org.springframework.stereotype.Component;
import ua.com.astone.acrm.dto.company.CompanyRequest;
import ua.com.astone.acrm.dto.company.CompanyResponse;
import ua.com.astone.acrm.model.Company;

@Component
public class CompanyMapper {

    public Company toEntity(CompanyRequest request) {
        return Company.builder()
                .name(request.getName())
                .industry(request.getIndustry())
                .address(request.getAddress())
                .website(request.getWebsite())
                .description(request.getDescription())
                .build();
    }

    public void updateEntity(CompanyRequest request, Company company) {
        company.setName(request.getName());
        company.setIndustry(request.getIndustry());
        company.setAddress(request.getAddress());
        company.setWebsite(request.getWebsite());
        company.setDescription(request.getDescription());
    }

    public CompanyResponse toResponse(Company company) {
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
