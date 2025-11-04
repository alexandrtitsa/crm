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
    @Builder.Default
    private int page = 0;
    @Builder.Default
    private int size = 10;
    @Builder.Default
    private String sort = "name";
    @Builder.Default
    private Sort.Direction direction = Sort.Direction.ASC;

    public Sort toSort() {

        Sort.Direction safeDirection = (direction == null) ? Sort.Direction.ASC : direction;
        String safeSort = (sort == null || sort.isBlank()) ? "name" : sort;
        return Sort.by(safeDirection, safeSort);
    }

    public void setPage(int page) {
        this.page = Math.max(0, page);
    }

    public void setSize(int size) {
        this.size = (size <= 0) ? 10 : size;
    }
}

