package ua.com.astone.acrm.controller.view;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ua.com.astone.acrm.dto.company.*;
import ua.com.astone.acrm.service.CompanyService;

@Controller
@RequestMapping("/companies")
@RequiredArgsConstructor
public class CompanyViewController {

    private final CompanyService companyService;

    @GetMapping
    public String listCompanies(@RequestParam(required = false) String search,
                                @RequestParam(defaultValue = "0") int page,
                                @RequestParam(defaultValue = "10") int size,
                                @RequestParam(defaultValue = "name") String sort,
                                @RequestParam(defaultValue = "ASC") String direction,
                                Model model) {

        var request = CompanyPageRequest.builder()
                .search(search)
                .page(page)
                .size(size)
                .sort(sort)
                .direction(org.springframework.data.domain.Sort.Direction.fromString(direction))
                .build();

        var companies = companyService.findAllPaged(request);

        model.addAttribute("companies", companies);
        model.addAttribute("request", request);
        return "company/list";
    }

    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("companyForm", new CompanyRequest());
        return "company/form";
    }

    @GetMapping("/{id}/edit")
    public String showEditForm(@PathVariable Long id, Model model) {
        var company = companyService.findById(id);

        CompanyRequest form = CompanyRequest.builder()
                .name(company.getName())
                .industry(company.getIndustry())
                .address(company.getAddress())
                .website(company.getWebsite())
                .description(company.getDescription())
                .build();

        model.addAttribute("companyForm", form);
        model.addAttribute("editId", id);
        return "company/form";
    }

    @PostMapping
    public String saveCompany(@ModelAttribute("companyForm") @Valid CompanyRequest form,
                              BindingResult bindingResult,
                              @RequestParam(required = false) Long editId,
                              Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("editId", editId);
            return "company/form";
        }

        if (editId != null) {
            companyService.update(editId, form);
        } else {
            companyService.create(form);
        }

        return "redirect:/companies";
    }

    @PostMapping("/{id}/delete")
    public String deleteCompany(@PathVariable Long id) {
        companyService.delete(id);
        return "redirect:/companies";
    }

}
