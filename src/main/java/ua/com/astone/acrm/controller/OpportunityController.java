package ua.com.astone.acrm.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ua.com.astone.acrm.dto.opportunity.OpportunityRequest;
import ua.com.astone.acrm.dto.opportunity.OpportunityResponse;
import ua.com.astone.acrm.service.OpportunityService;

import java.util.List;

@RestController
@RequestMapping("/api/opportunities")
@RequiredArgsConstructor
public class OpportunityController {

    private final OpportunityService service;

    @GetMapping
    public List<OpportunityResponse> findAll() {return service.findAll();}

    @GetMapping("/{id}")
    public OpportunityResponse findById(@PathVariable Long id) { return service.findById(id); }

    @PostMapping
    public OpportunityResponse create(@RequestBody @Valid OpportunityRequest request) {return service.create(request);}

    @PutMapping("/{id}")
    public OpportunityResponse update(@PathVariable Long id, @RequestBody @Valid OpportunityRequest request) {return service.update(id, request);}

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) { service.delete(id);}
}
