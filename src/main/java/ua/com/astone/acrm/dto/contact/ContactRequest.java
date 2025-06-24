package ua.com.astone.acrm.dto.contact;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ContactRequest {

    @NotBlank
    private String firstName;

    @NotBlank
    private String lastName;

    private String email;
    private String phone;
    private String position;
    private Long companyId; // ID компанії
}
