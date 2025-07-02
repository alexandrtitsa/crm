package ua.com.astone.acrm.dto.opportunity;

import jakarta.validation.constraints.*;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OpportunityRequest {
    @NotBlank(message = "Назва можливості обов'язкова")
    private String name;

    private Integer probability;
    private BigDecimal expectedValue;
    private String stage;

    @NotNull(message = "Лід обов'язковий")
    private Long leadId;
}

