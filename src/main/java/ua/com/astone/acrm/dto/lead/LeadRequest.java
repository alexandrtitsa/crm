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

    @NotBlank
    private Long id;
    private String source;

    @NotBlank
    private String status;

    @NotNull
    private Long contactId;
    private Long companyId;
}
