package ua.com.astone.acrm.controller.view;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ua.com.astone.acrm.service.CompanyImportService;

@Controller
@RequestMapping("/companies")
@RequiredArgsConstructor
public class CompanyImportController {

    private final CompanyImportService importService;

    @GetMapping("/import")
    public String showImportForm() {
        return "company/import";
    }

    @PostMapping("/import")
    public String handleImport(@RequestParam("file") MultipartFile file, RedirectAttributes redirectAttributes) {
        try {
            int count = importService.importCompanies(file);
            redirectAttributes.addFlashAttribute("successMessage", "Імпортовано компаній: " + count);
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Помилка імпорту: " + e.getMessage());
        }
        return "redirect:/companies";
    }
}
