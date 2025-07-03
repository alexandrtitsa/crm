package ua.com.astone.acrm.dto.analytics;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ProbabilityCountDto {
    private Integer probability;
    private Long count;
}