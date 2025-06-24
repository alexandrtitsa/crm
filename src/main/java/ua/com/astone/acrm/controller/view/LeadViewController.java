package ua.com.astone.acrm.controller.view;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ua.com.astone.acrm.dto.lead.LeadRequest;
import ua.com.astone.acrm.service.CompanyService;
import ua.com.astone.acrm.service.ContactService;
import ua.com.astone.acrm.service.LeadService;

@Controller
@RequestMapping("/leads")
@RequiredArgsConstructor
public class LeadViewController {

    private final LeadService leadService;
    private final ContactService contactService;
    private final CompanyService companyService;

    @GetMapping
    public String list(Model model) {
        model.addAttribute("leads", leadService.findAll());
        return "lead/list";
    }

    @GetMapping("/new")
    public String createForm(Model model) {
        model.addAttribute("leadForm", new LeadRequest());
        model.addAttribute("contacts", contactService.findAll());
        model.addAttribute("companies", companyService.findAll());
        return "lead/form";
    }

    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable Long id, Model model) {
        var lead = leadService.findById(id);
        var form = LeadRequest.builder()
                .source(lead.getSource())
                .status(lead.getStatus())
                .contactId(lead.getContactId())
                .companyId(lead.getCompanyId())
                .build();

        model.addAttribute("leadForm", form);
        model.addAttribute("contacts", contactService.findAll());
        model.addAttribute("companies", companyService.findAll());
        model.addAttribute("editId", id);
        return "lead/form";
    }

    @PostMapping
    public String save(@ModelAttribute("leadForm") @Valid LeadRequest form,
                       BindingResult bindingResult,
                       @RequestParam(required = false) Long editId,
                       Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("contacts", contactService.findAll());
            model.addAttribute("companies", companyService.findAll());
            model.addAttribute("editId", editId);
            return "lead/form";
        }

        if (editId != null) {
            leadService.update(editId, form);
        } else {
            leadService.create(form);
        }

        return "redirect:/leads";
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id) {
        leadService.delete(id);
        return "redirect:/leads";
    }
}
