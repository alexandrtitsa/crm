package ua.com.astone.acrm.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;
import ua.com.astone.acrm.dto.opportunity.OpportunityRequest;
import ua.com.astone.acrm.dto.opportunity.OpportunityResponse;
import ua.com.astone.acrm.service.OpportunityService;

@RestController
@RequestMapping("/api/opportunities")
@RequiredArgsConstructor
public class OpportunityController {

    private final OpportunityService opportunityService;

    @GetMapping
    public Page<OpportunityResponse> findAll(Pageable pageable) {
        return opportunityService.findAll(pageable);
    }

    @GetMapping("/{id}")
    public OpportunityResponse findById(@PathVariable Long id) {
        return opportunityService.findById(id);
    }

    @PostMapping
    public OpportunityResponse create(@RequestBody @Valid OpportunityRequest request) {
        return opportunityService.create(request);
    }

    @PutMapping("/{id}")
    public OpportunityResponse update(@PathVariable Long id, @RequestBody @Valid OpportunityRequest request) {
        return opportunityService.update(id, request);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        opportunityService.deleteById(id);
    }
}
