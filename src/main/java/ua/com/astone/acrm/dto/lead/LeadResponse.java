package ua.com.astone.acrm.dto.lead;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LeadResponse {

    private Long id;
    private String source;
    private String status;

    private Long contactId;
    private String contactName;

    private Long companyId;
    private String companyName;
}
