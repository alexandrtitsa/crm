package ua.com.astone.acrm.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ua.com.astone.acrm.dto.activity.ActivityRequest;
import ua.com.astone.acrm.dto.activity.ActivityResponse;
import ua.com.astone.acrm.service.ActivityService;

import java.util.List;

@RestController
@RequestMapping("/api/activities")
@RequiredArgsConstructor
public class ActivityController {

    private final ActivityService service;

    @GetMapping
    public List<ActivityResponse> findAll() {return service.findAll();}

    @GetMapping("/{id}")
    public ActivityResponse findById(@PathVariable Long id) {return service.findById(id);}

    @PostMapping
    public ActivityResponse create(@Valid @RequestBody ActivityRequest request) {return service.create(request);}

    @PutMapping("/{id}")
    public ActivityResponse update(@PathVariable Long id, @Valid @RequestBody ActivityRequest request) {return service.update(id, request);}

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {service.delete(id);}
}
