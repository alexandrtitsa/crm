package ua.com.astone.acrm.controller.view;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.ui.Model; // ← ДОДАЙ ЦЕЙ ІМПОРТ
import ua.com.astone.acrm.service.AnalyticsService;

@Controller
@RequiredArgsConstructor
@RequestMapping("/analytics")
public class AnalyticsViewController {
    private final AnalyticsService analyticsService;

    @GetMapping
    public String analytics(Model model) {
        model.addAttribute("leadsByStatus", analyticsService.countLeadsByStatus());
        model.addAttribute("opportunityByProb", analyticsService.countOpportunitiesByProbability());
        model.addAttribute("valueByStage", analyticsService.sumExpectedValueByStage());
        return "analytics";
    }
}
