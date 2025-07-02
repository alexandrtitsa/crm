package ua.com.astone.acrm.controller.view;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ua.com.astone.acrm.dto.company.CompanyPageRequest;
import ua.com.astone.acrm.dto.company.CompanyRequest;
import ua.com.astone.acrm.dto.company.CompanyResponse;
import ua.com.astone.acrm.service.CompanyService;

@Controller
@RequestMapping("/companies")
@RequiredArgsConstructor
public class CompanyViewController {

    private final CompanyService companyService;
    private static final String VIEW_FORM = "company/form";
    private static final String REDIRECT_LIST = "redirect:/companies";
    private static final String SUCCESS_MSG = "successMessage";
    private static final String ERROR_MSG = "errorMessage";

    @GetMapping
    public String listCompanies(
            @RequestParam(required = false) String search,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "name") String sort,
            @RequestParam(defaultValue = "ASC") String direction,
            Model model
    ) {
        CompanyPageRequest request = CompanyPageRequest.builder()
                .search(search)
                .page(page)
                .size(size)
                .sort(sort)
                .direction(Sort.Direction.fromString(direction))
                .build();

        var companies = companyService.findAllPaged(request);

        model.addAttribute("companies", companies);
        model.addAttribute("request", request);
        return "company/list";
    }

    @GetMapping("/new")
    public String showCreateFormCompanies(Model model) {
        model.addAttribute("companyForm", new CompanyRequest());
        return VIEW_FORM;
    }

    @PostMapping
    public String createCompany(
            @ModelAttribute("companyForm") @Valid CompanyRequest form,
            BindingResult bindingResult,
            Model model,
            RedirectAttributes redirectAttributes
    ) {
        if (bindingResult.hasErrors()) {
            return VIEW_FORM;
        }
        companyService.create(form);
        redirectAttributes.addFlashAttribute(SUCCESS_MSG, "Компанію створено успішно.");
        return REDIRECT_LIST;
    }

    @GetMapping("/{id}/edit")
    public String showEditFormCompanies(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        CompanyResponse company;
        try {
            company = companyService.findById(id);
        } catch (Exception ex) {
            redirectAttributes.addFlashAttribute(ERROR_MSG, "Компанію не знайдено.");
            return REDIRECT_LIST;
        }

        CompanyRequest form = CompanyRequest.builder()
                .name(company.getName())
                .industry(company.getIndustry())
                .address(company.getAddress())
                .website(company.getWebsite())
                .description(company.getDescription())
                .build();

        model.addAttribute("companyForm", form);
        model.addAttribute("editId", id);
        return VIEW_FORM;
    }

    @PostMapping("/{id}/edit")
    public String updateCompany(
            @PathVariable Long id,
            @ModelAttribute("companyForm") @Valid CompanyRequest form,
            BindingResult bindingResult,
            Model model,
            RedirectAttributes redirectAttributes
    ) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("editId", id);
            return VIEW_FORM;
        }

        try {
            companyService.update(id, form);
        } catch (Exception ex) {
            redirectAttributes.addFlashAttribute(ERROR_MSG, "Не вдалося оновити компанію.");
            return REDIRECT_LIST;
        }

        redirectAttributes.addFlashAttribute(SUCCESS_MSG, "Компанію оновлено успішно.");
        return REDIRECT_LIST;
    }

    @PostMapping("/{id}/delete")
    public String deleteCompany(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            companyService.delete(id);
            redirectAttributes.addFlashAttribute(SUCCESS_MSG, "Компанію видалено успішно.");
        } catch (Exception ex) {
            redirectAttributes.addFlashAttribute(ERROR_MSG, "Не вдалося видалити компанію.");
        }
        return REDIRECT_LIST;
    }
}
