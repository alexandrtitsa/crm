package ua.com.astone.acrm.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ua.com.astone.acrm.dto.company.CompanyRequest;
import ua.com.astone.acrm.dto.company.CompanyResponse;
import ua.com.astone.acrm.service.CompanyService;

import java.util.List;

@RestController
@RequestMapping("/api/companies")
@RequiredArgsConstructor
public class CompanyController {

    private final CompanyService service;

    @GetMapping
    public List<CompanyResponse> findAll() { return service.findAll(); }

    @GetMapping("/{id}")
    public CompanyResponse findById(@PathVariable Long id) {
        return service.findById(id);
    }

    @PostMapping
    public CompanyResponse create(@Valid @RequestBody CompanyRequest request) {
        return service.create(request);
    }

    @PutMapping("/{id}")
    public CompanyResponse update(@PathVariable Long id, @Valid @RequestBody CompanyRequest request) {
        return service.update(id, request);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }
}
