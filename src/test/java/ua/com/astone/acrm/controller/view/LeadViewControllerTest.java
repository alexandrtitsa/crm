package ua.com.astone.acrm.controller.view;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.beans.factory.annotation.Autowired;
import ua.com.astone.acrm.dto.lead.LeadPageRequest;
import ua.com.astone.acrm.dto.lead.LeadRequest;
import ua.com.astone.acrm.dto.lead.LeadResponse;
import ua.com.astone.acrm.dto.company.CompanyResponse;
import ua.com.astone.acrm.dto.contact.ContactResponse;
import ua.com.astone.acrm.service.CompanyService;
import ua.com.astone.acrm.service.ContactService;
import ua.com.astone.acrm.service.LeadService;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(LeadViewController.class)
class LeadViewControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private LeadService leadService;
    @MockBean
    private CompanyService companyService;
    @MockBean
    private ContactService contactService;

    @Test
    @WithMockUser
    @DisplayName("GET /leads — повертає сторінку зі списком лідів")
    void shouldReturnLeadListPage() throws Exception {
        Mockito.when(leadService.findAllPaged(any(LeadPageRequest.class)))
                .thenReturn(new PageImpl<>(List.of(
                        LeadResponse.builder()
                                .id(1L)
                                .source("web")
                                .status("new")
                                .build()
                ), PageRequest.of(0, 10, Sort.Direction.ASC, "source"), 1));

        mockMvc.perform(get("/leads"))
                .andExpect(status().isOk())
                .andExpect(view().name("lead/list"))
                .andExpect(model().attributeExists("leads"))
                .andExpect(model().attributeExists("request"));
    }

    @Test
    @WithMockUser
    @DisplayName("GET /leads/new — форма створення")
    void shouldShowCreateForm() throws Exception {
        Mockito.when(companyService.findAll()).thenReturn(List.of());
        Mockito.when(contactService.findAll()).thenReturn(List.of());

        mockMvc.perform(get("/leads/new"))
                .andExpect(status().isOk())
                .andExpect(view().name("lead/form"))
                .andExpect(model().attributeExists("leadForm"))
                .andExpect(model().attributeExists("companies"))
                .andExpect(model().attributeExists("contacts"));
    }

    @Test
    @WithMockUser
    @DisplayName("POST /leads — створення нового ліда")
    void shouldCreateLeadAndRedirect() throws Exception {
        Mockito.when(leadService.create(any(LeadRequest.class)))
                .thenReturn(LeadResponse.builder().id(101L).build());

        mockMvc.perform(post("/leads")
                        .param("source", "web")
                        .param("status", "new")
                        .param("contactId", "1")
                        .param("companyId", "2")
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/leads"));
    }

    @Test
    @WithMockUser
    @DisplayName("GET /leads/{id}/edit — форма редагування")
    void shouldShowEditForm() throws Exception {
        LeadResponse response = LeadResponse.builder()
                .id(2L)
                .source("web")
                .status("new")
                .contactId(1L)
                .companyId(2L)
                .build();

        Mockito.when(leadService.findById(eq(2L))).thenReturn(response);
        Mockito.when(companyService.findAll()).thenReturn(List.of(CompanyResponse.builder().id(2L).name("TestCompany").build()));
        Mockito.when(contactService.findAll()).thenReturn(List.of(ContactResponse.builder().id(1L).firstName("Іван").build()));

        mockMvc.perform(get("/leads/2/edit"))
                .andExpect(status().isOk())
                .andExpect(view().name("lead/form"))
                .andExpect(model().attributeExists("leadForm"))
                .andExpect(model().attributeExists("companies"))
                .andExpect(model().attributeExists("contacts"))
                .andExpect(model().attributeExists("editId"));
    }

    @Test
    @WithMockUser
    @DisplayName("POST /leads/{id}/edit — оновлення ліда")
    void shouldUpdateLeadAndRedirect() throws Exception {
        Mockito.when(leadService.update(eq(3L), any(LeadRequest.class)))
                .thenReturn(LeadResponse.builder().id(3L).build());

        mockMvc.perform(post("/leads/3/edit")
                        .param("source", "web")
                        .param("status", "converted")
                        .param("contactId", "1")
                        .param("companyId", "2")
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/leads"));
    }

    @Test
    @WithMockUser
    @DisplayName("POST /leads/{id}/delete — видалення ліда")
    void shouldDeleteLeadAndRedirect() throws Exception {
        mockMvc.perform(post("/leads/4/delete").with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/leads"));
    }
}
