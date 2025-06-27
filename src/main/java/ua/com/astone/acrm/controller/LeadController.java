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

    private final LeadService leadService;

    @GetMapping
    public List<LeadResponse> findAll() {
        return leadService.findAll();
    }

    @GetMapping("/{id}")
    public LeadResponse findById(@PathVariable Long id) {
        return leadService.findById(id);
    }

    @PostMapping
    public LeadResponse create(@RequestBody LeadRequest request) {
        return leadService.create(request);
    }

    @PutMapping("/{id}")
    public LeadResponse update(@PathVariable Long id, @RequestBody LeadRequest request) {
        return leadService.update(id, request);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        leadService.deleteById(id);
    }
}
