package ua.com.astone.acrm.util;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ua.com.astone.acrm.dto.activity.ActivityRequest;
import ua.com.astone.acrm.dto.activity.ActivityResponse;
import ua.com.astone.acrm.model.Activity;
import ua.com.astone.acrm.model.Contact;
import ua.com.astone.acrm.model.Opportunity;
import ua.com.astone.acrm.repository.ContactRepository;
import ua.com.astone.acrm.repository.OpportunityRepository;

@Component
@RequiredArgsConstructor
public class ActivityMapper {

    private final ContactRepository contactRepository;
    private final OpportunityRepository opportunityRepository;

    public Activity toEntity(ActivityRequest request) {
        return Activity.builder()
                .type(request.getType())
                .dateTime(request.getDateTime())
                .description(request.getDescription())
                .contact(resolveContact(request.getContactId()))
                .opportunity(resolveOpportunity(request.getOpportunityId()))
                .build();
    }

    public void updateEntity(ActivityRequest request, Activity activity) {
        activity.setType(request.getType());
        activity.setDateTime(request.getDateTime());
        activity.setDescription(request.getDescription());
        activity.setContact(resolveContact(request.getContactId()));
        activity.setOpportunity(resolveOpportunity(request.getOpportunityId()));
    }

    public ActivityResponse toResponse(Activity activity) {
        return ActivityResponse.builder()
                .id(activity.getId())
                .type(activity.getType())
                .dateTime(activity.getDateTime())
                .description(activity.getDescription())
                .contactId(activity.getContact() != null ? activity.getContact().getId() : null)
                .contactFirstName(activity.getContact() != null ? activity.getContact().getFirstName() : null)
                .contactLastName(activity.getContact() != null ? activity.getContact().getLastName() : null)
                .opportunityId(activity.getOpportunity() != null ? activity.getOpportunity().getId() : null)
                .opportunityName(activity.getOpportunity() != null ? activity.getOpportunity().getName() : null)
                .build();
    }

    private Contact resolveContact(Long id) {
        return id != null ? contactRepository.findById(id).orElse(null) : null;
    }

    private Opportunity resolveOpportunity(Long id) {
        return id != null ? opportunityRepository.findById(id).orElse(null) : null;
    }
}
