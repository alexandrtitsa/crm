package ua.com.astone.acrm.controller.view;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.beans.factory.annotation.Autowired;
import ua.com.astone.acrm.dto.activity.ActivityPageRequest;
import ua.com.astone.acrm.dto.activity.ActivityRequest;
import ua.com.astone.acrm.dto.activity.ActivityResponse;
import ua.com.astone.acrm.service.ActivityService;
import ua.com.astone.acrm.service.ContactService;
import ua.com.astone.acrm.service.OpportunityService;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ActivityViewController.class)
@AutoConfigureMockMvc(addFilters = false)
@Import(ActivityViewControllerTest.MockConfig.class) // <<< тут твоя тестова конфігурація
class ActivityViewControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ActivityService activityService;

    @Autowired
    private ContactService contactService;

    @Autowired
    private OpportunityService opportunityService;

    @TestConfiguration
    static class MockConfig {
        @Bean ActivityService activityService() { return Mockito.mock(ActivityService.class); }
        @Bean ContactService contactService() { return Mockito.mock(ContactService.class); }
        @Bean OpportunityService opportunityService() { return Mockito.mock(OpportunityService.class); }
    }

    @Test
    @DisplayName("GET /activities — повертає сторінку зі списком")
    void shouldReturnActivityListPage() throws Exception {
        Mockito.when(activityService.findAllPaged(any(ActivityPageRequest.class)))
                .thenReturn(new PageImpl<>(List.of(
                        ActivityResponse.builder()
                                .id(1L)
                                .type("Call")
                                .dateTime(LocalDateTime.now())
                                .build()
                ), PageRequest.of(0, 10, Sort.Direction.DESC, "dateTime"), 1));

        mockMvc.perform(get("/activities"))
                .andExpect(status().isOk())
                .andExpect(view().name("activity/list"))
                .andExpect(model().attributeExists("activities"))
                .andExpect(model().attributeExists("request"));
    }

    @Test
    @DisplayName("GET /activities/new — форма створення")
    void shouldShowCreateForm() throws Exception {
        Mockito.when(contactService.findAll()).thenReturn(List.of());
        Mockito.when(opportunityService.findAll()).thenReturn(List.of());

        mockMvc.perform(get("/activities/new"))
                .andExpect(status().isOk())
                .andExpect(view().name("activity/form"))
                .andExpect(model().attributeExists("activityForm"))
                .andExpect(model().attributeExists("contacts"))
                .andExpect(model().attributeExists("opportunities"));
    }

    @Test
    @DisplayName("POST /activities — створення нової активності")
    void shouldCreateActivityAndRedirect() throws Exception {
        Mockito.when(activityService.create(any(ActivityRequest.class)))
                .thenReturn(ActivityResponse.builder().id(100L).build());

        mockMvc.perform(post("/activities")
                        .param("type", "Meeting")
                        .param("description", "Important")
                        .param("dateTime", "2025-07-10T10:00")
                        .param("contactId", "1")
                        .param("opportunityId", "1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/activities"));
    }

    @Test
    @DisplayName("GET /activities/{id}/edit — форма редагування: існує")
    void shouldShowEditForm() throws Exception {
        ActivityResponse response = ActivityResponse.builder()
                .id(2L)
                .type("Call")
                .dateTime(LocalDateTime.now())
                .build();

        Mockito.when(activityService.findById(2L)).thenReturn(response);
        Mockito.when(contactService.findAll()).thenReturn(List.of());
        Mockito.when(opportunityService.findAll()).thenReturn(List.of());

        mockMvc.perform(get("/activities/2/edit"))
                .andExpect(status().isOk())
                .andExpect(view().name("activity/form"))
                .andExpect(model().attributeExists("activityForm"))
                .andExpect(model().attributeExists("editId"))
                .andExpect(model().attributeExists("contacts"))
                .andExpect(model().attributeExists("opportunities"));
    }

    @Test
    @DisplayName("GET /activities/{id}/edit — форма редагування: не знайдено")
    void shouldRedirectWhenEditFormNotFound() throws Exception {
        Mockito.when(activityService.findById(404L))
                .thenThrow(new IllegalArgumentException("Not found"));

        mockMvc.perform(get("/activities/404/edit"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/activities"));
    }

    @Test
    @DisplayName("GET /activities/calendar — сторінка календаря")
    void shouldShowCalendarPage() throws Exception {
        Mockito.when(activityService.findAll()).thenReturn(List.of());

        mockMvc.perform(get("/activities/calendar"))
                .andExpect(status().isOk())
                .andExpect(view().name("activity/calendar"))
                .andExpect(model().attributeExists("activities"));
    }
}
