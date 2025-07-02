package ua.com.astone.acrm.dto.contact;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ContactRequest {
    @NotBlank(message = "Ім'я обов'язкове")
    private String firstName;

    private String lastName;
    private String email;
    private String phone;
    private String position;

    @NotNull(message = "Компанія обов'язкова")
    private Long companyId;
}
