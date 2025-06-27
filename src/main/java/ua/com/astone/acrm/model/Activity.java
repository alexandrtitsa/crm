package ua.com.astone.acrm.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "activities")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Activity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String type;

    private LocalDateTime dateTime;

    private String description;

    @ManyToOne
    @JoinColumn(name = "contact_id")
    private Contact contact;

    @ManyToOne
    @JoinColumn(name = "opportunity_id")
    private Opportunity opportunity;
}
