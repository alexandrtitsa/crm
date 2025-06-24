package ua.com.astone.acrm.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import ua.com.astone.acrm.dto.opportunity.*;
import ua.com.astone.acrm.service.OpportunityService;

import java.util.List;

@RestController
@RequestMapping("/api/opportunities")
@RequiredArgsConstructor
public class OpportunityController {

    private final OpportunityService opportunityService;

    @PostMapping
    public ResponseEntity<OpportunityResponse> create(@RequestBody @Valid OpportunityRequest request) {
        return new ResponseEntity<>(opportunityService.create(request), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<OpportunityResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(opportunityService.findById(id));
    }

    @GetMapping
    public ResponseEntity<List<OpportunityResponse>> getAll() {
        return ResponseEntity.ok(opportunityService.findAll());
    }

    @PutMapping("/{id}")
    public ResponseEntity<OpportunityResponse> update(@PathVariable Long id,
                                                      @RequestBody @Valid OpportunityRequest request) {
        return ResponseEntity.ok(opportunityService.update(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        opportunityService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
