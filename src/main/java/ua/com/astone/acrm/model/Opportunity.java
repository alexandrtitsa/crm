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

    @Column(precision = 12, scale = 2)
    private BigDecimal expectedValue;

    private String stage;

    @OneToOne
    @JoinColumn(name = "lead_id", unique = true)
    private Lead lead;
}
