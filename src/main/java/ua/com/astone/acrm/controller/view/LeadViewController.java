package ua.com.astone.acrm.controller.view;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
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
    private final CompanyService companyService;
    private final ContactService contactService;

    @GetMapping
    public String list(Model model) {
        model.addAttribute("leads", leadService.findAll());
        return "lead/list";
    }

    @GetMapping("/add")
    public String addForm(Model model) {
        model.addAttribute("lead", new LeadRequest());
        model.addAttribute("companies", companyService.findAll());
        model.addAttribute("contacts", contactService.findAll());
        return "lead/form";
    }

    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable Long id, Model model) {
        model.addAttribute("lead", leadService.findById(id));
        model.addAttribute("companies", companyService.findAll());
        model.addAttribute("contacts", contactService.findAll());
        return "lead/form";
    }

    @PostMapping
    public String save(@ModelAttribute LeadRequest request) {
        leadService.create(request);
        return "redirect:/leads";
    }

    @PostMapping("/{id}")
    public String update(@PathVariable Long id, @ModelAttribute LeadRequest request) {
        leadService.update(id, request);
        return "redirect:/leads";
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id) {
        leadService.deleteById(id);
        return "redirect:/leads";
    }
}
