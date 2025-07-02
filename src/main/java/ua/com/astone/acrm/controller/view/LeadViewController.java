package ua.com.astone.acrm.controller.view;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ua.com.astone.acrm.dto.lead.LeadPageRequest;
import ua.com.astone.acrm.dto.lead.LeadRequest;
import ua.com.astone.acrm.dto.lead.LeadResponse;
import ua.com.astone.acrm.service.CompanyService;
import ua.com.astone.acrm.service.ContactService;
import ua.com.astone.acrm.service.LeadService;

@Controller
@RequestMapping("/leads")
@RequiredArgsConstructor
public class LeadViewController {

    private final LeadService leadService;
    private final CompanyService companyService;
    private final ContactService contactService;
    private static final String VIEW_FORM = "lead/form";
    private static final String REDIRECT_LIST = "redirect:/leads";
    private static final String COMPANIES = "companies";
    private static final String CONTACTS = "contacts";
    private static final String SUCCESS_MSG = "successMessage";
    private static final String ERROR_MSG = "errorMessage";

    @GetMapping
    public String listLeads(
            @RequestParam(required = false) String search,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "source") String sort,
            @RequestParam(defaultValue = "ASC") String direction,
            Model model
    ) {
        LeadPageRequest request = LeadPageRequest.builder()
                .search(search)
                .page(page)
                .size(size)
                .sort(sort)
                .direction(Sort.Direction.fromString(direction))
                .build();

        var leads = leadService.findAllPaged(request);

        model.addAttribute("leads", leads);
        model.addAttribute("request", request);
        return "lead/list";
    }

    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("leadForm", new LeadRequest());
        model.addAttribute(COMPANIES, companyService.findAll());
        model.addAttribute(CONTACTS, contactService.findAll());
        return VIEW_FORM;
    }

    @PostMapping
    public String createLead(
            @ModelAttribute("leadForm") @Valid LeadRequest form,
            BindingResult bindingResult,
            Model model,
            RedirectAttributes redirectAttributes
    ) {
        if (bindingResult.hasErrors()) {
            model.addAttribute(COMPANIES, companyService.findAll());
            model.addAttribute(CONTACTS, contactService.findAll());
            return VIEW_FORM;
        }
        leadService.create(form);
        redirectAttributes.addFlashAttribute(SUCCESS_MSG, "Лід створено успішно.");
        return REDIRECT_LIST;
    }

    @GetMapping("/{id}/edit")
    public String showEditForm(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        LeadResponse lead;
        try {
            lead = leadService.findById(id);
        } catch (Exception ex) {
            redirectAttributes.addFlashAttribute(ERROR_MSG, "Лід не знайдено.");
            return REDIRECT_LIST;
        }
        LeadRequest form = LeadRequest.builder()
                .source(lead.getSource())
                .status(lead.getStatus())
                .contactId(lead.getContactId())
                .companyId(lead.getCompanyId())
                .build();

        model.addAttribute("leadForm", form);
        model.addAttribute(COMPANIES, companyService.findAll());
        model.addAttribute(CONTACTS, contactService.findAll());
        model.addAttribute("editId", id);
        return VIEW_FORM;
    }

    @PostMapping("/{id}/edit")
    public String updateLead(
            @PathVariable Long id,
            @ModelAttribute("leadForm") @Valid LeadRequest form,
            BindingResult bindingResult,
            Model model,
            RedirectAttributes redirectAttributes
    ) {
        if (bindingResult.hasErrors()) {
            model.addAttribute(COMPANIES, companyService.findAll());
            model.addAttribute(CONTACTS, contactService.findAll());
            model.addAttribute("editId", id);
            return VIEW_FORM;
        }
        try {
            leadService.update(id, form);
        } catch (Exception ex) {
            redirectAttributes.addFlashAttribute(ERROR_MSG, "Не вдалося оновити лід.");
            return REDIRECT_LIST;
        }
        redirectAttributes.addFlashAttribute(SUCCESS_MSG, "Лід оновлено успішно.");
        return REDIRECT_LIST;
    }

    @PostMapping("/{id}/delete")
    public String deleteLead(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            leadService.delete(id);
            redirectAttributes.addFlashAttribute(SUCCESS_MSG, "Лід видалено успішно.");
        } catch (Exception ex) {
            redirectAttributes.addFlashAttribute(ERROR_MSG, "Не вдалося видалити лід.");
        }
        return REDIRECT_LIST;
    }
}
