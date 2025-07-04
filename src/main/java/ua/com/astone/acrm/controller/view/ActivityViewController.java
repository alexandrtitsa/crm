package ua.com.astone.acrm.controller.view;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ua.com.astone.acrm.dto.activity.ActivityPageRequest;
import ua.com.astone.acrm.dto.activity.ActivityRequest;
import ua.com.astone.acrm.dto.activity.ActivityResponse;
import ua.com.astone.acrm.service.ActivityService;
import ua.com.astone.acrm.service.ContactService;
import ua.com.astone.acrm.service.OpportunityService;

@Controller
@RequestMapping("/activities")
@RequiredArgsConstructor
public class ActivityViewController {

    private final ActivityService activityService;
    private final ContactService contactService;
    private final OpportunityService opportunityService;
    private static final String VIEW_FORM = "activity/form";
    private static final String REDIRECT_LIST = "redirect:/activities";
    private static final String SUCCESS_MSG = "successMessage";
    private static final String ERROR_MSG = "errorMessage";
    private static final String CONTACTS_ATTR = "contacts";
    private static final String OPPORTUNITIES_ATTR = "opportunities";

    @GetMapping
    public String listActivities(
            @RequestParam(required = false) String search,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "dateTime") String sort,
            @RequestParam(defaultValue = "DESC") String direction,
            Model model
    ) {
        ActivityPageRequest request = ActivityPageRequest.builder()
                .search(search)
                .page(page)
                .size(size)
                .sort(sort)
                .direction(Sort.Direction.fromString(direction))
                .build();

        var activities = activityService.findAllPaged(request);

        model.addAttribute("activities", activities);
        model.addAttribute("request", request);
        return "activity/list";
    }

    @GetMapping("/new")
    public String showCreateFormActivities(Model model) {
        model.addAttribute("activityForm", new ActivityRequest());
        model.addAttribute(CONTACTS_ATTR, contactService.findAll());
        model.addAttribute(OPPORTUNITIES_ATTR, opportunityService.findAll());
        return VIEW_FORM;
    }

    @PostMapping
    public String createActivity(
            @ModelAttribute("activityForm") @Valid ActivityRequest form,
            BindingResult bindingResult,
            Model model,
            RedirectAttributes redirectAttributes
    ) {
        if (bindingResult.hasErrors()) {
            model.addAttribute(CONTACTS_ATTR, contactService.findAll());
            model.addAttribute(OPPORTUNITIES_ATTR, opportunityService.findAll());
            return VIEW_FORM;
        }
        activityService.create(form);
        redirectAttributes.addFlashAttribute(SUCCESS_MSG, "Активність створено успішно.");
        return REDIRECT_LIST;
    }

    @GetMapping("/{id}/edit")
    public String showEditFormActivities(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        ActivityResponse activity;
        try {
            activity = activityService.findById(id);
        } catch (Exception ex) {
            redirectAttributes.addFlashAttribute(ERROR_MSG, "Активність не знайдено.");
            return REDIRECT_LIST;
        }

        ActivityRequest form = ActivityRequest.builder()
                .type(activity.getType())
                .dateTime(activity.getDateTime())
                .description(activity.getDescription())
                .contactId(activity.getContactId())
                .opportunityId(activity.getOpportunityId())
                .build();

        model.addAttribute("activityForm", form);
        model.addAttribute("editId", id);
        model.addAttribute(CONTACTS_ATTR, contactService.findAll());
        model.addAttribute(OPPORTUNITIES_ATTR, opportunityService.findAll());
        return VIEW_FORM;
    }

    @PostMapping("/{id}/edit")
    public String updateActivity(
            @PathVariable Long id,
            @ModelAttribute("activityForm") @Valid ActivityRequest form,
            BindingResult bindingResult,
            Model model,
            RedirectAttributes redirectAttributes
    ) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("editId", id);
            model.addAttribute(CONTACTS_ATTR, contactService.findAll());
            model.addAttribute(OPPORTUNITIES_ATTR, opportunityService.findAll());
            return VIEW_FORM;
        }

        try {
            activityService.update(id, form);
        } catch (Exception ex) {
            redirectAttributes.addFlashAttribute(ERROR_MSG, "Не вдалося оновити активність.");
            return REDIRECT_LIST;
        }

        redirectAttributes.addFlashAttribute(SUCCESS_MSG, "Активність оновлено успішно.");
        return REDIRECT_LIST;
    }

    @PostMapping("/{id}/delete")
    public String deleteActivity(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            activityService.delete(id);
            redirectAttributes.addFlashAttribute(SUCCESS_MSG, "Активність видалено успішно.");
        } catch (Exception ex) {
            redirectAttributes.addFlashAttribute(ERROR_MSG, "Не вдалося видалити активність.");
        }
        return REDIRECT_LIST;
    }

    @GetMapping("/calendar")
    public String showCalendar(Model model) {
        model.addAttribute("activities", activityService.findAll());
        return "activity/calendar";
    }
}
