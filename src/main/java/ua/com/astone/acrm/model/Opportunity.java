package ua.com.astone.acrm.model;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "opportunities")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Opportunity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private Integer probability;
    private BigDecimal expectedValue;
    private String stage;

    @ManyToOne
    @JoinColumn(name = "lead_id")
    private Lead lead;
}
