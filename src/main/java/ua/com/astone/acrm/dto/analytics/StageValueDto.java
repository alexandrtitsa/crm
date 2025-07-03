package ua.com.astone.acrm.dto.analytics;

import lombok.AllArgsConstructor;
import lombok.Data;
import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class StageValueDto {
    private String stage;
    private BigDecimal sum;
}
