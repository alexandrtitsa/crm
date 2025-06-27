package ua.com.astone.acrm.controller.view;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ua.com.astone.acrm.model.Contact;
import ua.com.astone.acrm.service.CompanyService;
import ua.com.astone.acrm.service.ContactService;

@Controller
@RequiredArgsConstructor
@RequestMapping("/contacts")
public class ContactViewController {

    private final ContactService contactService;
    private final CompanyService companyService;

    @GetMapping
    public String list(Model model) {
        model.addAttribute("contacts", contactService.findAll());
        return "contact/list";
    }

    @GetMapping("/add")
    public String showAddForm(Model model) {
        model.addAttribute("contact", new Contact());
        model.addAttribute("companies", companyService.findAll());
        return "contact/form";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        model.addAttribute("contact", contactService.findByIdEntity(id));
        model.addAttribute("companies", companyService.findAll());
        return "contact/form";
    }

    @PostMapping
    public String save(@ModelAttribute Contact contact) {
        contactService.save(contact);
        return "redirect:/contacts";
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id) {
        contactService.deleteById(id);
        return "redirect:/contacts";
    }
}
