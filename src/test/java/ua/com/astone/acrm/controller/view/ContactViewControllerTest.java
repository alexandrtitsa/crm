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
import ua.com.astone.acrm.dto.contact.ContactPageRequest;
import ua.com.astone.acrm.dto.contact.ContactRequest;
import ua.com.astone.acrm.dto.contact.ContactResponse;
import ua.com.astone.acrm.dto.company.CompanyResponse;
import ua.com.astone.acrm.service.CompanyService;
import ua.com.astone.acrm.service.ContactService;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ContactViewController.class)
class ContactViewControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ContactService contactService;

    @MockBean
    private CompanyService companyService;

    @Test
    @WithMockUser
    @DisplayName("GET /contacts — повертає сторінку зі списком контактів")
    void shouldReturnContactListPage() throws Exception {
        Mockito.when(contactService.findAllPaged(any(ContactPageRequest.class)))
                .thenReturn(new PageImpl<>(List.of(
                        ContactResponse.builder()
                                .id(1L)
                                .firstName("Іван")
                                .lastName("Петров")
                                .build()
                ), PageRequest.of(0, 10, Sort.Direction.ASC, "lastName"), 1));

        mockMvc.perform(get("/contacts"))
                .andExpect(status().isOk())
                .andExpect(view().name("contact/list"))
                .andExpect(model().attributeExists("contacts"))
                .andExpect(model().attributeExists("request"));
    }

    @Test
    @WithMockUser
    @DisplayName("GET /contacts/new — форма створення")
    void shouldShowCreateForm() throws Exception {
        Mockito.when(companyService.findAll()).thenReturn(List.of());

        mockMvc.perform(get("/contacts/new"))
                .andExpect(status().isOk())
                .andExpect(view().name("contact/form"))
                .andExpect(model().attributeExists("contactForm"))
                .andExpect(model().attributeExists("companies"));
    }

    @Test
    @WithMockUser
    @DisplayName("POST /contacts — створення нового контакту")
    void shouldCreateContactAndRedirect() throws Exception {
        Mockito.when(contactService.create(any(ContactRequest.class)))
                .thenReturn(ContactResponse.builder().id(100L).build());

        mockMvc.perform(post("/contacts")
                        .param("firstName", "Test")
                        .param("lastName", "Contact")
                        .param("email", "test@example.com")
                        .param("phone", "+380991234567")
                        .param("position", "Manager")
                        .param("companyId", "1") // якщо обовʼязкове
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/contacts"));
    }

    @Test
    @WithMockUser
    @DisplayName("GET /contacts/{id}/edit — форма редагування")
    void shouldShowEditForm() throws Exception {
        ContactResponse response = ContactResponse.builder()
                .id(2L)
                .firstName("Test")
                .lastName("User")
                .email("test@example.com")
                .phone("+380999")
                .position("Position")
                .companyId(5L)
                .build();

        Mockito.when(contactService.findById(eq(2L))).thenReturn(response);
        Mockito.when(companyService.findAll()).thenReturn(List.of(CompanyResponse.builder().id(5L).name("TestCompany").build()));

        mockMvc.perform(get("/contacts/2/edit"))
                .andExpect(status().isOk())
                .andExpect(view().name("contact/form"))
                .andExpect(model().attributeExists("contactForm"))
                .andExpect(model().attributeExists("editId"))
                .andExpect(model().attributeExists("companies"));
    }

    @Test
    @WithMockUser
    @DisplayName("POST /contacts/{id}/edit — оновлення контакту")
    void shouldUpdateContactAndRedirect() throws Exception {
        Mockito.when(contactService.update(eq(3L), any(ContactRequest.class)))
                .thenReturn(ContactResponse.builder().id(3L).build());

        mockMvc.perform(post("/contacts/3/edit")
                        .param("firstName", "Updated")
                        .param("lastName", "User")
                        .param("email", "updated@example.com")
                        .param("phone", "+380999")
                        .param("position", "NewPos")
                        .param("companyId", "1")
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/contacts"));
    }

    @Test
    @WithMockUser
    @DisplayName("POST /contacts/{id}/delete — видалення контакту")
    void shouldDeleteContactAndRedirect() throws Exception {
        mockMvc.perform(post("/contacts/4/delete").with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/contacts"));
    }
}
