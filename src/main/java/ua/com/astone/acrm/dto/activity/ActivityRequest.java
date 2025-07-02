package ua.com.astone.acrm.dto.activity;

import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ActivityRequest {
    @NotBlank(message = "Тип активності обов'язковий")
    private String type;

    private LocalDateTime dateTime;

    private String description;

    @NotNull(message = "Контакт обов'язковий")
    private Long contactId;

    @NotNull(message = "Можливість обов'язкова")
    private Long opportunityId;
}
