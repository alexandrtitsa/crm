package ua.com.astone.acrm.dto.lead;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LeadRequest {

    private Long id;

    private String source;

    @NotBlank(message = "Статус обов'язковий")
    private String status;

    @NotNull(message = "Контакт обов'язковий")
    private Long contactId;

    @NotNull(message = "Компанія обов'язкова")
    private Long companyId;
}
