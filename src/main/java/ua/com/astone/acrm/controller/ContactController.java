package ua.com.astone.acrm.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;
import ua.com.astone.acrm.dto.contact.ContactRequest;
import ua.com.astone.acrm.dto.contact.ContactResponse;
import ua.com.astone.acrm.service.ContactService;

import java.util.List;

@RestController
@RequestMapping("/api/contacts")
@RequiredArgsConstructor
public class ContactController {

    private final ContactService contactService;

    @GetMapping
    public List<ContactResponse> findAll() {
        return contactService.findAll();
    }

    @GetMapping("/paged")
    public List<ContactResponse> findAllPaged(Pageable pageable) {
        return contactService.findAll(pageable).getContent();
    }

    @GetMapping("/{id}")
    public ContactResponse findById(@PathVariable Long id) {
        return contactService.findById(id);
    }

    @PostMapping
    public ContactResponse create(@RequestBody ContactRequest request) {
        return contactService.create(request);
    }

    @PutMapping("/{id}")
    public ContactResponse update(@PathVariable Long id, @RequestBody ContactRequest request) {
        return contactService.update(id, request);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        contactService.deleteById(id);
    }
}
