package ua.com.astone.acrm.service;

import ua.com.astone.acrm.dto.company.*;
import org.springframework.data.domain.Page;
import java.util.List;

public interface CompanyService {
    CompanyResponse findById(Long id);
    List<CompanyResponse> findAll();
    Page<CompanyResponse> findAllPaged(CompanyPageRequest request);
    CompanyResponse create(CompanyRequest request);
    CompanyResponse update(Long id, CompanyRequest request);
    void delete(Long id);
}
