package ua.com.astone.acrm.service;

import ua.com.astone.acrm.dto.activity.*;

import java.util.List;

public interface ActivityService {
    ActivityResponse create(ActivityRequest request);
    ActivityResponse findById(Long id);
    List<ActivityResponse> findAll();
    ActivityResponse update(Long id, ActivityRequest request);
    void delete(Long id);
}
