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

    @NotBlank
    private String name;

    @Min(0)
    @Max(100)
    private Integer probability;

    @DecimalMin(value = "0.0", inclusive = true)
    private BigDecimal expectedValue;

    @NotBlank
    private String stage;

    @NotNull
    private Long leadId;
}
