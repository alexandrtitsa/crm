package ua.com.astone.acrm.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ua.com.astone.acrm.dto.activity.ActivityRequest;
import ua.com.astone.acrm.dto.activity.ActivityResponse;

import java.util.List;

public interface ActivityService {
    List<ActivityResponse> findAll();
    Page<ActivityResponse> findAll(Pageable pageable);
    ActivityResponse findById(Long id);
    ActivityResponse create(ActivityRequest request);
    ActivityResponse update(Long id, ActivityRequest request);
    void deleteById(Long id);
}
