package ua.com.astone.acrm.dto.company;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CompanyRequest {

    @NotBlank
    private String name;

    private String industry;
    private String address;
    private String website;
    private String description;
}
