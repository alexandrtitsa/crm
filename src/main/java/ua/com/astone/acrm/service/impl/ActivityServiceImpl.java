package ua.com.astone.acrm.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.com.astone.acrm.dto.activity.ActivityRequest;
import ua.com.astone.acrm.dto.activity.ActivityResponse;
import ua.com.astone.acrm.exception.NotFoundException;
import ua.com.astone.acrm.model.Activity;
import ua.com.astone.acrm.repository.ActivityRepository;
import ua.com.astone.acrm.service.ActivityService;
import ua.com.astone.acrm.util.ActivityMapper;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ActivityServiceImpl implements ActivityService {

    private final ActivityRepository repository;
    private final ActivityMapper mapper;

    @Override
    public List<ActivityResponse> findAll() {
        return repository.findAll().stream().map(mapper::toResponse).toList();
    }

    @Override
    public Page<ActivityResponse> findAll(Pageable pageable) {
        return repository.findAll(pageable).map(mapper::toResponse);
    }

    @Override
    public ActivityResponse findById(Long id) {
        return mapper.toResponse(findByIdOrThrow(id));
    }

    @Override
    @Transactional
    public ActivityResponse create(ActivityRequest request) {
        return mapper.toResponse(repository.save(mapper.toEntity(request)));
    }

    @Override
    @Transactional
    public ActivityResponse update(Long id, ActivityRequest request) {
        Activity activity = findByIdOrThrow(id);
        mapper.updateEntity(request, activity);
        return mapper.toResponse(repository.save(activity));
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        repository.deleteById(id);
    }

    private Activity findByIdOrThrow(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new NotFoundException("Activity", id));
    }
}
