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

    private Long opportunityId;
    private String opportunityName;

    private Long contactId;
    private String contactName;
}
