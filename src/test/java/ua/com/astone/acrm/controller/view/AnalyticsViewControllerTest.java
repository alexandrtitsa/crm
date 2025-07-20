package ua.com.astone.acrm.controller.view;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.beans.factory.annotation.Autowired;
import ua.com.astone.acrm.dto.analytics.ProbabilityCountDto;
import ua.com.astone.acrm.dto.analytics.StageValueDto;
import ua.com.astone.acrm.dto.analytics.StatusCountDto;
import ua.com.astone.acrm.service.AnalyticsService;

import java.math.BigDecimal;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AnalyticsViewController.class)
@AutoConfigureMockMvc(addFilters = false)
class AnalyticsViewControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private AnalyticsService analyticsService;

    @TestConfiguration
    static class MockConfig {
        @Bean AnalyticsService analyticsService() {
            return Mockito.mock(AnalyticsService.class);}
    }

    @Test
    @DisplayName("GET /analytics — сторінка аналітики відображається коректно")
    void shouldReturnAnalyticsPage() throws Exception {

        when(analyticsService.countLeadsByStatus()).thenReturn(List.of(
                new StatusCountDto("new", 5L),
                new StatusCountDto("contacted", 2L)
        ));
        when(analyticsService.countOpportunitiesByProbability()).thenReturn(List.of(
                new ProbabilityCountDto(80, 3L)
        ));
        when(analyticsService.sumExpectedValueByStage()).thenReturn(List.of(
                new StageValueDto("Initial", BigDecimal.valueOf(1000))
        ));

        mockMvc.perform(get("/analytics"))
                .andExpect(status().isOk())
                .andExpect(view().name("analytics"))
                .andExpect(model().attributeExists("leadsByStatus"))
                .andExpect(model().attributeExists("opportunityByProb"))
                .andExpect(model().attributeExists("valueByStage"));
    }
}
