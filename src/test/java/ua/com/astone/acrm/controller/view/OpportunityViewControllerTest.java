package ua.com.astone.acrm.controller.view;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.beans.factory.annotation.Autowired;
import ua.com.astone.acrm.dto.opportunity.OpportunityPageRequest;
import ua.com.astone.acrm.dto.opportunity.OpportunityRequest;
import ua.com.astone.acrm.dto.opportunity.OpportunityResponse;
import ua.com.astone.acrm.dto.lead.LeadResponse;
import ua.com.astone.acrm.service.LeadService;
import ua.com.astone.acrm.service.OpportunityService;

import java.math.BigDecimal;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(OpportunityViewController.class)
class OpportunityViewControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private OpportunityService opportunityService;
    @Autowired
    private LeadService leadService;

    @TestConfiguration
    static class MockConfig {
        @Bean OpportunityService opportunityService() { return Mockito.mock(OpportunityService.class); }
        @Bean
        LeadService leadService() { return Mockito.mock(LeadService.class); }
    }

    @Test
    @WithMockUser
    @DisplayName("GET /opportunities — повертає сторінку зі списком можливостей")
    void shouldReturnOpportunityListPage() throws Exception {
        Mockito.when(opportunityService.findAllPaged(any(OpportunityPageRequest.class)))
                .thenReturn(new PageImpl<>(List.of(
                        OpportunityResponse.builder()
                                .id(1L)
                                .name("Big Deal")
                                .probability(90)
                                .expectedValue(BigDecimal.valueOf(10000))
                                .build()
                ), PageRequest.of(0, 10, Sort.Direction.ASC, "name"), 1));

        mockMvc.perform(get("/opportunities"))
                .andExpect(status().isOk())
                .andExpect(view().name("opportunity/list"))
                .andExpect(model().attributeExists("opportunities"))
                .andExpect(model().attributeExists("request"));
    }

    @Test
    @WithMockUser
    @DisplayName("GET /opportunities/new — форма створення")
    void shouldShowCreateForm() throws Exception {
        Mockito.when(leadService.findAll()).thenReturn(List.of());

        mockMvc.perform(get("/opportunities/new"))
                .andExpect(status().isOk())
                .andExpect(view().name("opportunity/form"))
                .andExpect(model().attributeExists("opportunityForm"))
                .andExpect(model().attributeExists("leads"));
    }

    @Test
    @WithMockUser
    @DisplayName("POST /opportunities — створення нової можливості")
    void shouldCreateOpportunityAndRedirect() throws Exception {
        Mockito.when(opportunityService.create(any(OpportunityRequest.class)))
                .thenReturn(OpportunityResponse.builder().id(101L).build());

        mockMvc.perform(post("/opportunities")
                        .param("name", "New Opp")
                        .param("probability", "75")
                        .param("expectedValue", "5000")
                        .param("stage", "Negotiation")
                        .param("leadId", "1")
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/opportunities"));
    }

    @Test
    @WithMockUser
    @DisplayName("GET /opportunities/{id}/edit — форма редагування")
    void shouldShowEditForm() throws Exception {
        OpportunityResponse response = OpportunityResponse.builder()
                .id(2L)
                .name("Mega Deal")
                .probability(80)
                .expectedValue(BigDecimal.valueOf(20000))
                .stage("Proposal")
                .leadId(1L)
                .build();

        Mockito.when(opportunityService.findById(2L)).thenReturn(response);
        Mockito.when(leadService.findAll()).thenReturn(List.of(
                LeadResponse.builder().id(1L).source("web").build()
        ));

        mockMvc.perform(get("/opportunities/2/edit"))
                .andExpect(status().isOk())
                .andExpect(view().name("opportunity/form"))
                .andExpect(model().attributeExists("opportunityForm"))
                .andExpect(model().attributeExists("editId"))
                .andExpect(model().attributeExists("leads"));
    }

    @Test
    @WithMockUser
    @DisplayName("POST /opportunities/{id}/edit — оновлення можливості")
    void shouldUpdateOpportunityAndRedirect() throws Exception {
        Mockito.when(opportunityService.update(eq(3L), any(OpportunityRequest.class)))
                .thenReturn(OpportunityResponse.builder().id(3L).build());

        mockMvc.perform(post("/opportunities/3/edit")
                        .param("name", "Updated Opp")
                        .param("probability", "90")
                        .param("expectedValue", "30000")
                        .param("stage", "Closed Won")
                        .param("leadId", "2")
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/opportunities"));
    }

    @Test
    @WithMockUser
    @DisplayName("POST /opportunities/{id}/delete — видалення можливості")
    void shouldDeleteOpportunityAndRedirect() throws Exception {
        mockMvc.perform(post("/opportunities/5/delete").with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/opportunities"));
    }
}
