package ua.com.astone.acrm.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import ua.com.astone.acrm.dto.lead.*;
import ua.com.astone.acrm.service.LeadService;

import java.util.List;

@RestController
@RequestMapping("/api/leads")
@RequiredArgsConstructor
public class LeadController {

    private final LeadService leadService;

    @PostMapping
    public ResponseEntity<LeadResponse> create(@RequestBody @Valid LeadRequest request) {
        return new ResponseEntity<>(leadService.create(request), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<LeadResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(leadService.findById(id));
    }

    @GetMapping
    public ResponseEntity<List<LeadResponse>> getAll() {
        return ResponseEntity.ok(leadService.findAll());
    }

    @PutMapping("/{id}")
    public ResponseEntity<LeadResponse> update(@PathVariable Long id,
                                               @RequestBody @Valid LeadRequest request) {
        return ResponseEntity.ok(leadService.update(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        leadService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
