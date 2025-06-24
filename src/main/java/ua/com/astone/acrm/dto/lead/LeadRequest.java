package ua.com.astone.acrm.dto.lead;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LeadRequest {

    @NotBlank
    private String source;

    @NotBlank
    private String status;

    private Long contactId;
    private Long companyId;
}
