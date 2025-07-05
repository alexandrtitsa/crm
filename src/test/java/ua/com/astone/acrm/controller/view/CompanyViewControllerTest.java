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
import ua.com.astone.acrm.dto.company.CompanyPageRequest;
import ua.com.astone.acrm.dto.company.CompanyRequest;
import ua.com.astone.acrm.dto.company.CompanyResponse;
import ua.com.astone.acrm.service.CompanyService;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CompanyViewController.class)
class CompanyViewControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CompanyService companyService;

    @Test
    @WithMockUser
    @DisplayName("GET /companies — повертає сторінку зі списком компаній")
    void shouldReturnCompanyListPage() throws Exception {
        Mockito.when(companyService.findAllPaged(any(CompanyPageRequest.class)))
                .thenReturn(new PageImpl<>(List.of(
                        CompanyResponse.builder()
                                .id(1L)
                                .name("Acme Inc")
                                .industry("IT")
                                .build()
                ), PageRequest.of(0, 10, Sort.Direction.ASC, "name"), 1));

        mockMvc.perform(get("/companies"))
                .andExpect(status().isOk())
                .andExpect(view().name("company/list"))
                .andExpect(model().attributeExists("companies"))
                .andExpect(model().attributeExists("request"));
    }

    @Test
    @WithMockUser
    @DisplayName("GET /companies/new — форма створення")
    void shouldShowCreateForm() throws Exception {
        mockMvc.perform(get("/companies/new"))
                .andExpect(status().isOk())
                .andExpect(view().name("company/form"))
                .andExpect(model().attributeExists("companyForm"));
    }

    @Test
    @WithMockUser
    @DisplayName("POST /companies — створення нової компанії")
    void shouldCreateCompanyAndRedirect() throws Exception {
        Mockito.when(companyService.create(any(CompanyRequest.class)))
                .thenReturn(CompanyResponse.builder().id(100L).build());

        mockMvc.perform(post("/companies")
                        .param("name", "Test Company")
                        .param("industry", "IT")
                        .param("address", "Ukraine")
                        .param("website", "www.test.com")
                        .param("description", "Test")
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/companies"));
    }

    @Test
    @WithMockUser
    @DisplayName("GET /companies/{id}/edit — форма редагування")
    void shouldShowEditForm() throws Exception {
        CompanyResponse response = CompanyResponse.builder()
                .id(2L)
                .name("EditMe")
                .industry("IT")
                .address("UA")
                .website("site.com")
                .description("desc")
                .build();

        Mockito.when(companyService.findById(eq(2L))).thenReturn(response);

        mockMvc.perform(get("/companies/2/edit"))
                .andExpect(status().isOk())
                .andExpect(view().name("company/form"))
                .andExpect(model().attributeExists("companyForm"))
                .andExpect(model().attributeExists("editId"));
    }

    @Test
    @WithMockUser
    @DisplayName("POST /companies/{id}/edit — оновлення компанії")
    void shouldUpdateCompanyAndRedirect() throws Exception {
        Mockito.when(companyService.update(eq(3L), any(CompanyRequest.class)))
                .thenReturn(CompanyResponse.builder().id(3L).build());

        mockMvc.perform(post("/companies/3/edit")
                        .param("name", "Updated")
                        .param("industry", "IT")
                        .param("address", "Kyiv")
                        .param("website", "upd.com")
                        .param("description", "upd")
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/companies"));
    }

    @Test
    @WithMockUser
    @DisplayName("POST /companies/{id}/delete — видалення компанії")
    void shouldDeleteCompanyAndRedirect() throws Exception {
        mockMvc.perform(post("/companies/4/delete").with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/companies"));
    }
}
