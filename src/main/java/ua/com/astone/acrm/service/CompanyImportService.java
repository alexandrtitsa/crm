package ua.com.astone.acrm.service;

import org.springframework.web.multipart.MultipartFile;

public interface CompanyImportService {
    int importCompanies(MultipartFile file) throws Exception;
}
