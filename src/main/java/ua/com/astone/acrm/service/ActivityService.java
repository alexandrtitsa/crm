package ua.com.astone.acrm.service;

import ua.com.astone.acrm.dto.activity.*;
import org.springframework.data.domain.Page;
import java.util.List;

public interface ActivityService {
    ActivityResponse findById(Long id);
    List<ActivityResponse> findAll();
    Page<ActivityResponse> findAllPaged(ActivityPageRequest request);
    ActivityResponse create(ActivityRequest request);
    ActivityResponse update(Long id, ActivityRequest request);
    void delete(Long id);
}
