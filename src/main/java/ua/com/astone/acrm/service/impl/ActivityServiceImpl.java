package ua.com.astone.acrm.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.com.astone.acrm.dto.activity.*;
import ua.com.astone.acrm.model.*;
import ua.com.astone.acrm.repository.*;
import ua.com.astone.acrm.service.ActivityService;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ActivityServiceImpl implements ActivityService {

    private final ActivityRepository activityRepository;
    private final OpportunityRepository opportunityRepository;
    private final ContactRepository contactRepository;

    @Override
    public ActivityResponse create(ActivityRequest request) {
        Activity activity = toEntity(request);
        return toDto(activityRepository.save(activity));
    }

    @Override
    public ActivityResponse findById(Long id) {
        return toDto(activityRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Activity not found")));
    }

    @Override
    public List<ActivityResponse> findAll() {
        return activityRepository.findAll().stream()
                .map(this::toDto)
                .toList();
    }

    @Override
    public ActivityResponse update(Long id, ActivityRequest request) {
        Activity activity = activityRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Activity not found"));

        activity.setType(request.getType());
        activity.setDateTime(request.getDateTime());
        activity.setDescription(request.getDescription());
        activity.setOpportunity(getOpportunityById(request.getOpportunityId()));
        activity.setContact(getContactById(request.getContactId()));

        return toDto(activityRepository.save(activity));
    }

    @Override
    public void delete(Long id) {
        activityRepository.deleteById(id);
    }

    private Activity toEntity(ActivityRequest r) {
        return Activity.builder()
                .type(r.getType())
                .dateTime(r.getDateTime())
                .description(r.getDescription())
                .opportunity(getOpportunityById(r.getOpportunityId()))
                .contact(getContactById(r.getContactId()))
                .build();
    }

    private ActivityResponse toDto(Activity a) {
        return ActivityResponse.builder()
                .id(a.getId())
                .type(a.getType())
                .dateTime(a.getDateTime())
                .description(a.getDescription())
                .opportunityId(a.getOpportunity() != null ? a.getOpportunity().getId() : null)
                .opportunityName(a.getOpportunity() != null ? a.getOpportunity().getName() : null)
                .contactId(a.getContact() != null ? a.getContact().getId() : null)
                .contactName(a.getContact() != null
                        ? a.getContact().getFirstName() + " " + a.getContact().getLastName()
                        : null)
                .build();
    }

    private Opportunity getOpportunityById(Long id) {
        if (id == null) return null;
        return opportunityRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Opportunity not found"));
    }

    private Contact getContactById(Long id) {
        if (id == null) return null;
        return contactRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Contact not found"));
    }
}
