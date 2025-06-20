package ua.com.astone.acrm.dto.company;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CompanyResponse {

    private Long id;
    private String name;
    private String industry;
    private String address;
    private String website;
    private String description;
}
