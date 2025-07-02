package ua.com.astone.acrm.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ua.com.astone.acrm.dto.lead.LeadRequest;
import ua.com.astone.acrm.dto.lead.LeadResponse;
import ua.com.astone.acrm.service.LeadService;

import java.util.List;

@RestController
@RequestMapping("/api/leads")
@RequiredArgsConstructor
public class LeadController {

    private final LeadService service;

    @GetMapping
    public List<LeadResponse> findAll() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    public LeadResponse findById(@PathVariable Long id) {
        return service.findById(id);
    }

    @PostMapping
    public LeadResponse create(@RequestBody LeadRequest request) {
        return service.create(request);
    }

    @PutMapping("/{id}")
    public LeadResponse update(@PathVariable Long id, @RequestBody LeadRequest request) {
        return service.update(id, request);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }
}
