package ua.com.astone.acrm.dto.company;

import lombok.*;
import org.springframework.data.domain.Sort;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CompanyPageRequest {
    private String search;
    private int page = 0;
    private int size = 10;
    private String sort = "name";
    private Sort.Direction direction = Sort.Direction.ASC;

    public Sort getSortOrder() {
        return Sort.by(direction, sort);
    }
}
