package ua.com.astone.acrm.dto.activity;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ActivityResponse {

    private Long id;
    private String type;
    private LocalDateTime dateTime;
    private String description;
    private Long contactId;
    private String contactFirstName;
    private String contactLastName;
    private Long opportunityId;
    private String opportunityName;
}
