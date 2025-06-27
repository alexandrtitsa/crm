package ua.com.astone.acrm.dto.opportunity;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OpportunityResponse {

    private Long id;
    private String name;
    private Integer probability;
    private BigDecimal expectedValue;
    private String stage;
    private Long leadId;
}
