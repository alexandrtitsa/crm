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

    @GetMapping("/new")
    public String createForm(Model model) {
        model.addAttribute("opportunityForm", new OpportunityRequest());
        model.addAttribute("leads", leadService.findAll());
        return "opportunity/form";
    }

    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable Long id, Model model) {
        var opportunity = opportunityService.findById(id);
        var form = OpportunityRequest.builder()
                .name(opportunity.getName())
                .probability(opportunity.getProbability())
                .expectedValue(opportunity.getExpectedValue())
                .stage(opportunity.getStage())
                .leadId(opportunity.getLeadId())
                .build();

        model.addAttribute("opportunityForm", form);
        model.addAttribute("leads", leadService.findAll());
        model.addAttribute("editId", id);
        return "opportunity/form";
    }

    @PostMapping
    public String save(@ModelAttribute("opportunityForm") @Valid OpportunityRequest form,
                       BindingResult result,
                       @RequestParam(required = false) Long editId,
                       Model model) {
        if (result.hasErrors()) {
            model.addAttribute("leads", leadService.findAll());
            model.addAttribute("editId", editId);
            return "opportunity/form";
        }

        if (editId != null) {
            opportunityService.update(editId, form);
        } else {
            opportunityService.create(form);
        }

        return "redirect:/opportunities";
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id) {
        opportunityService.delete(id);
        return "redirect:/opportunities";
    }
}
