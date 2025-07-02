package ua.com.astone.acrm.controller.view;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ua.com.astone.acrm.dto.opportunity.OpportunityPageRequest;
import ua.com.astone.acrm.dto.opportunity.OpportunityRequest;
import ua.com.astone.acrm.dto.opportunity.OpportunityResponse;
import ua.com.astone.acrm.service.LeadService;
import ua.com.astone.acrm.service.OpportunityService;

@Controller
@RequestMapping("/opportunities")
@RequiredArgsConstructor
public class OpportunityViewController {

    private final OpportunityService opportunityService;
    private final LeadService leadService;
    private static final String VIEW_FORM = "opportunity/form";
    private static final String REDIRECT_LIST = "redirect:/opportunities";
    private static final String SUCCESS_MSG = "successMessage";
    private static final String ERROR_MSG = "errorMessage";
    private static final String LEADS_ATTR = "leads";

    @GetMapping
    public String listOpportunities(
            @RequestParam(required = false) String search,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "name") String sort,
            @RequestParam(defaultValue = "ASC") String direction,
            Model model
    ) {
        OpportunityPageRequest request = OpportunityPageRequest.builder()
                .search(search)
                .page(page)
                .size(size)
                .sort(sort)
                .direction(Sort.Direction.fromString(direction))
                .build();

        var opportunities = opportunityService.findAllPaged(request);

        model.addAttribute("opportunities", opportunities);
        model.addAttribute("request", request);
        return "opportunity/list";
    }

    @GetMapping("/new")
    public String showCreateFormOpportunities(Model model) {
        model.addAttribute("opportunityForm", new OpportunityRequest());
        model.addAttribute(LEADS_ATTR, leadService.findAll());
        return VIEW_FORM;
    }

    @PostMapping
    public String createOpportunity(
            @ModelAttribute("opportunityForm") @Valid OpportunityRequest form,
            BindingResult bindingResult,
            Model model,
            RedirectAttributes redirectAttributes
    ) {
        if (bindingResult.hasErrors()) {
            model.addAttribute(LEADS_ATTR, leadService.findAll());
            return VIEW_FORM;
        }
        opportunityService.create(form);
        redirectAttributes.addFlashAttribute(SUCCESS_MSG, "Можливість створено успішно.");
        return REDIRECT_LIST;
    }

    @GetMapping("/{id}/edit")
    public String showEditFormOpportunities(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        OpportunityResponse opportunity;
        try {
            opportunity = opportunityService.findById(id);
        } catch (Exception ex) {
            redirectAttributes.addFlashAttribute(ERROR_MSG, "Можливість не знайдено.");
            return REDIRECT_LIST;
        }

        OpportunityRequest form = OpportunityRequest.builder()
                .name(opportunity.getName())
                .probability(opportunity.getProbability())
                .expectedValue(opportunity.getExpectedValue())
                .stage(opportunity.getStage())
                .leadId(opportunity.getLeadId())
                .build();

        model.addAttribute("opportunityForm", form);
        model.addAttribute("editId", id);
        model.addAttribute(LEADS_ATTR, leadService.findAll());
        return VIEW_FORM;
    }

    @PostMapping("/{id}/edit")
    public String updateOpportunity(
            @PathVariable Long id,
            @ModelAttribute("opportunityForm") @Valid OpportunityRequest form,
            BindingResult bindingResult,
            Model model,
            RedirectAttributes redirectAttributes
    ) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("editId", id);
            model.addAttribute(LEADS_ATTR, leadService.findAll());
            return VIEW_FORM;
        }

        try {
            opportunityService.update(id, form);
        } catch (Exception ex) {
            redirectAttributes.addFlashAttribute(ERROR_MSG, "Не вдалося оновити можливість.");
            return REDIRECT_LIST;
        }

        redirectAttributes.addFlashAttribute(SUCCESS_MSG, "Можливість оновлено успішно.");
        return REDIRECT_LIST;
    }

    @PostMapping("/{id}/delete")
    public String deleteOpportunity(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            opportunityService.delete(id);
            redirectAttributes.addFlashAttribute(SUCCESS_MSG, "Можливість видалено успішно.");
        } catch (Exception ex) {
            redirectAttributes.addFlashAttribute(ERROR_MSG, "Не вдалося видалити можливість.");
        }
        return REDIRECT_LIST;
    }
}
