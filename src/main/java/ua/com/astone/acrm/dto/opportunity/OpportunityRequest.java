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


    private Long id;

    @NotBlank
    private String name;

    @Min(0)
    @Max(100)
    private Integer probability;

    @DecimalMin("0.0")
    private BigDecimal expectedValue;

    @NotBlank
    private String stage;

    private Long leadId;
}
