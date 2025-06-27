package ua.com.astone.acrm.controller.view;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ua.com.astone.acrm.dto.opportunity.OpportunityRequest;
import ua.com.astone.acrm.service.LeadService;
import ua.com.astone.acrm.service.OpportunityService;

@Controller
@RequestMapping("/opportunities")
@RequiredArgsConstructor
public class OpportunityViewController {

    private final OpportunityService opportunityService;
    private final LeadService leadService;

    @GetMapping
    public String list(Model model) {
        model.addAttribute("opportunities", opportunityService.findAll());
        return "opportunity/list";
    }

    @GetMapping("/add")
    public String showAddForm(Model model) {
        model.addAttribute("opportunity", new OpportunityRequest());
        model.addAttribute("leads", leadService.findAll());
        return "opportunity/form";
    }

    @GetMapping("/{id}/edit")
    public String showEditForm(@PathVariable Long id, Model model) {
        model.addAttribute("opportunity", opportunityService.findById(id));
        model.addAttribute("leads", leadService.findAll());
        return "opportunity/form";
    }

    @PostMapping
    public String save(@Valid @ModelAttribute("opportunity") OpportunityRequest request, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("leads", leadService.findAll());
            return "opportunity/form";
        }
        opportunityService.create(request);
        return "redirect:/opportunities";
    }

    @PostMapping("/{id}")
    public String update(@PathVariable Long id, @Valid @ModelAttribute("opportunity") OpportunityRequest request, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("leads", leadService.findAll());
            return "opportunity/form";
        }
        opportunityService.update(id, request);
        return "redirect:/opportunities";
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id) {
        opportunityService.deleteById(id);
        return "redirect:/opportunities";
    }
}
