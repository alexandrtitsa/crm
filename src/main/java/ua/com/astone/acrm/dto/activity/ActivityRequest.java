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

    private Long id;

    @NotBlank
    private String type;

    @NotNull
    private LocalDateTime dateTime;

    private String description;

    private Long contactId;

    private Long opportunityId;
}
