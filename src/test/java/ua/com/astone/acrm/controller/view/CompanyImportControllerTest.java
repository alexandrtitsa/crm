package ua.com.astone.acrm.controller.view;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.beans.factory.annotation.Autowired;
import ua.com.astone.acrm.service.CompanyImportService;
import org.springframework.security.test.context.support.WithMockUser;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CompanyImportController.class)
class CompanyImportControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CompanyImportService importService;

    @TestConfiguration
    static class MockConfig {
        @Bean CompanyImportService importService() {
            return Mockito.mock(CompanyImportService.class);}
    }

    @Test
    @DisplayName("GET /companies/import — повертає форму імпорту")
    @WithMockUser
    void showImportForm_returnsFormView() throws Exception {
        mockMvc.perform(get("/companies/import"))
                .andExpect(status().isOk())
                .andExpect(view().name("company/import"));
    }


    @Test
    @WithMockUser
    void handleImport_successfulImport() throws Exception {
        MockMultipartFile file = new MockMultipartFile(
                "file", "companies.csv", "text/csv",
                "name;industry;address;website;description\nA;B;C;D;E".getBytes()
        );

        Mockito.when(importService.importCompanies(any())).thenReturn(1);

        mockMvc.perform(multipart("/companies/import")
                        .file(file)
                        .with(csrf()) // <== ДОДАЙ ЦЕ!
                )
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/companies"))
                .andExpect(flash().attributeExists("successMessage"));
    }

}
