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

    private final ActivityService service;
    private final ContactService contactService;
    private final OpportunityService opportunityService;

    @GetMapping
    public String list(Model model) {
        model.addAttribute("activities", service.findAll());
        return "activity/list";
    }

    @GetMapping("/add")
    public String addForm(Model model) {
        model.addAttribute("activity", new ActivityRequest());
        model.addAttribute("contacts", contactService.findAll());
        model.addAttribute("opportunities", opportunityService.findAll());
        return "activity/form";
    }

    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable Long id, Model model) {
        model.addAttribute("activity", service.findById(id));
        model.addAttribute("contacts", contactService.findAll());
        model.addAttribute("opportunities", opportunityService.findAll());
        return "activity/form";
    }

    @PostMapping
    public String save(@Valid @ModelAttribute("activity") ActivityRequest request, BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("contacts", contactService.findAll());
            model.addAttribute("opportunities", opportunityService.findAll());
            return "activity/form";
        }
        service.create(request);
        return "redirect:/activities";
    }

    @PostMapping("/{id}")
    public String update(@PathVariable Long id, @Valid @ModelAttribute("activity") ActivityRequest request, BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("contacts", contactService.findAll());
            model.addAttribute("opportunities", opportunityService.findAll());
            return "activity/form";
        }
        service.update(id, request);
        return "redirect:/activities";
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id) {
        service.deleteById(id);
        return "redirect:/activities";
    }
}
