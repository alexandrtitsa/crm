package ua.com.astone.acrm.controller.view;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ua.com.astone.acrm.dto.activity.ActivityRequest;
import ua.com.astone.acrm.service.ActivityService;
import ua.com.astone.acrm.service.ContactService;
import ua.com.astone.acrm.service.OpportunityService;

@Controller
@RequestMapping("/activities")
@RequiredArgsConstructor
public class ActivityViewController {

    private final ActivityService activityService;
    private final OpportunityService opportunityService;
    private final ContactService contactService;

    @GetMapping
    public String list(Model model) {
        model.addAttribute("activities", activityService.findAll());
        return "activity/list";
    }

    @GetMapping("/new")
    public String createForm(Model model) {
        model.addAttribute("activityForm", new ActivityRequest());
        model.addAttribute("opportunities", opportunityService.findAll());
        model.addAttribute("contacts", contactService.findAll());
        return "activity/form";
    }

    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable Long id, Model model) {
        var a = activityService.findById(id);
        var form = ActivityRequest.builder()
                .type(a.getType())
                .description(a.getDescription())
                .dateTime(a.getDateTime())
                .opportunityId(a.getOpportunityId())
                .contactId(a.getContactId())
                .build();

        model.addAttribute("activityForm", form);
        model.addAttribute("opportunities", opportunityService.findAll());
        model.addAttribute("contacts", contactService.findAll());
        model.addAttribute("editId", id);
        return "activity/form";
    }

    @PostMapping
    public String save(@ModelAttribute("activityForm") @Valid ActivityRequest form,
                       BindingResult result,
                       @RequestParam(required = false) Long editId,
                       Model model) {
        if (result.hasErrors()) {
            model.addAttribute("opportunities", opportunityService.findAll());
            model.addAttribute("contacts", contactService.findAll());
            model.addAttribute("editId", editId);
            return "activity/form";
        }

        if (editId != null) {
            activityService.update(editId, form);
        } else {
            activityService.create(form);
        }

        return "redirect:/activities";
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id) {
        activityService.delete(id);
        return "redirect:/activities";
    }
}
