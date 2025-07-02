package ua.com.astone.acrm.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.com.astone.acrm.dto.activity.*;
import ua.com.astone.acrm.model.Activity;
import ua.com.astone.acrm.repository.ActivityRepository;
import ua.com.astone.acrm.service.ActivityService;
import ua.com.astone.acrm.util.ActivityMapper;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ActivityServiceImpl implements ActivityService {

    private final ActivityRepository activityRepository;
    private final ActivityMapper activityMapper;

    @Override
    public ActivityResponse findById(Long id) {
        return activityRepository.findById(id)
                .map(activityMapper::toResponse)
                .orElseThrow(() -> new IllegalArgumentException("Activity not found"));
    }

    @Override
    public List<ActivityResponse> findAll() {
        return activityRepository.findAll().stream()
                .map(activityMapper::toResponse)
                .toList();
    }

    @Override
    public Page<ActivityResponse> findAllPaged(ActivityPageRequest request) {
        Pageable pageable = PageRequest.of(request.getPage(), request.getSize(), request.toSort());

        Page<Activity> page;
        if (request.getSearch() != null && !request.getSearch().isBlank()) {
            String q = request.getSearch().toLowerCase();
            page = activityRepository.findByTypeIgnoreCaseContainingOrDescriptionIgnoreCaseContaining(q, q, pageable);
        } else {
            page = activityRepository.findAll(pageable);
        }

        return page.map(activityMapper::toResponse);
    }

    @Override
    public ActivityResponse create(ActivityRequest request) {
        Activity activity = activityMapper.toEntity(request);
        return activityMapper.toResponse(activityRepository.save(activity));
    }

    @Override
    public ActivityResponse update(Long id, ActivityRequest request) {
        Activity activity = activityRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Activity not found"));
        activityMapper.updateEntity(request, activity);
        return activityMapper.toResponse(activityRepository.save(activity));
    }

    @Override
    public void delete(Long id) {
        activityRepository.deleteById(id);
    }
}
