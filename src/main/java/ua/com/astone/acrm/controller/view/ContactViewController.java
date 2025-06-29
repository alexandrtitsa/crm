package ua.com.astone.acrm.controller.view;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ua.com.astone.acrm.dto.contact.ContactRequest;
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
        model.addAttribute("contact", new ContactRequest());
        model.addAttribute("companies", companyService.findAll());
        return "contact/form";
    }

    @GetMapping("/{id}/edit")
    public String showEditForm(@PathVariable Long id, Model model) {
        var contact = contactService.findById(id);

        // ContactRequest для форми
        ContactRequest form = ContactRequest.builder()
                .firstName(contact.getFirstName())
                .lastName(contact.getLastName())
                .email(contact.getEmail())
                .phone(contact.getPhone())
                .position(contact.getPosition())
                .companyId(contact.getCompanyId())
                .build();

        model.addAttribute("contact", form);
        model.addAttribute("companies", companyService.findAll());
        model.addAttribute("editId", id);
        return "contact/form";
    }

    @PostMapping
    public String save(
            @ModelAttribute("contact") @Valid ContactRequest form,
            @RequestParam(value = "editId", required = false) Long editId,
            BindingResult result,
            Model model
    ) {
        if (result.hasErrors()) {
            model.addAttribute("companies", companyService.findAll());
            model.addAttribute("editId", editId);
            return "contact/form";
        }
        if (editId != null) {
            contactService.update(editId, form);
        } else {
            contactService.create(form);
        }
        return "redirect:/contacts";
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id) {
        contactService.deleteById(id);
        return "redirect:/contacts";
    }
}
