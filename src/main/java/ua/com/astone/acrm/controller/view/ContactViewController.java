package ua.com.astone.acrm.controller.view;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ua.com.astone.acrm.dto.contact.ContactPageRequest;
import ua.com.astone.acrm.dto.contact.ContactRequest;
import ua.com.astone.acrm.dto.contact.ContactResponse;
import ua.com.astone.acrm.service.CompanyService;
import ua.com.astone.acrm.service.ContactService;

@Controller
@RequestMapping("/contacts")
@RequiredArgsConstructor
public class ContactViewController {

    private final ContactService contactService;
    private final CompanyService companyService;
    private static final String VIEW_FORM = "contact/form";
    private static final String REDIRECT_LIST = "redirect:/contacts";
    private static final String SUCCESS_MSG = "successMessage";
    private static final String ERROR_MSG = "errorMessage";
    private static final String COMPANIES_ATTR = "companies";

    @GetMapping
    public String listContacts(
            @RequestParam(required = false) String search,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "lastName") String sort,
            @RequestParam(defaultValue = "ASC") String direction,
            Model model
    ) {
        ContactPageRequest request = ContactPageRequest.builder()
                .search(search)
                .page(page)
                .size(size)
                .sort(sort)
                .direction(org.springframework.data.domain.Sort.Direction.fromString(direction))
                .build();

        var contacts = contactService.findAllPaged(request);

        model.addAttribute("contacts", contacts);
        model.addAttribute("request", request);
        return "contact/list";
    }

    @GetMapping("/new")
    public String showCreateFormContacts(Model model) {
        model.addAttribute("contactForm", new ContactRequest());
        model.addAttribute(COMPANIES_ATTR, companyService.findAll());
        return VIEW_FORM;
    }

    @PostMapping
    public String createContact(
            @ModelAttribute("contactForm") @Valid ContactRequest form,
            BindingResult bindingResult,
            Model model,
            RedirectAttributes redirectAttributes
    ) {
        if (bindingResult.hasErrors()) {
            model.addAttribute(COMPANIES_ATTR, companyService.findAll());
            return VIEW_FORM;
        }
        contactService.create(form);
        redirectAttributes.addFlashAttribute(SUCCESS_MSG, "Контакт створено успішно.");
        return REDIRECT_LIST;
    }

    @GetMapping("/{id}/edit")
    public String showEditFormContacts(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        ContactResponse contact;
        try {
            contact = contactService.findById(id);
        } catch (Exception ex) {
            redirectAttributes.addFlashAttribute(ERROR_MSG, "Контакт не знайдено.");
            return REDIRECT_LIST;
        }

        ContactRequest form = ContactRequest.builder()
                .firstName(contact.getFirstName())
                .lastName(contact.getLastName())
                .email(contact.getEmail())
                .phone(contact.getPhone())
                .position(contact.getPosition())
                .companyId(contact.getCompanyId())
                .build();

        model.addAttribute("contactForm", form);
        model.addAttribute("editId", id);
        model.addAttribute(COMPANIES_ATTR, companyService.findAll());
        return VIEW_FORM;
    }

    @PostMapping("/{id}/edit")
    public String updateContact(
            @PathVariable Long id,
            @ModelAttribute("contactForm") @Valid ContactRequest form,
            BindingResult bindingResult,
            Model model,
            RedirectAttributes redirectAttributes
    ) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("editId", id);
            model.addAttribute(COMPANIES_ATTR, companyService.findAll());
            return VIEW_FORM;
        }

        try {
            contactService.update(id, form);
        } catch (Exception ex) {
            redirectAttributes.addFlashAttribute(ERROR_MSG, "Не вдалося оновити контакт.");
            return REDIRECT_LIST;
        }

        redirectAttributes.addFlashAttribute(SUCCESS_MSG, "Контакт оновлено успішно.");
        return REDIRECT_LIST;
    }

    @PostMapping("/{id}/delete")
    public String deleteContact(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            contactService.delete(id);
            redirectAttributes.addFlashAttribute(SUCCESS_MSG, "Контакт видалено успішно.");
        } catch (Exception ex) {
            redirectAttributes.addFlashAttribute(ERROR_MSG, "Не вдалося видалити контакт.");
        }
        return REDIRECT_LIST;
    }
}
