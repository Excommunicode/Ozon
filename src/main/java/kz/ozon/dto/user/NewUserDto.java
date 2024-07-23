package kz.ozon.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import kz.ozon.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class NewUserDto {

    @NotBlank
    @NotNull(message = "The field name must not be null")
    @Size(min = 2, max = 250, message = "Name must be between 2 and 250 characters long")
    private String name;

    @Email(message = "Invalid email address format")
    @NotEmpty(message = "Email address cannot be empty")
    @Size(min = 6, max = 254, message = "Email address must be between 6 and 254 characters long")
    private String email;

    @NotNull(message = "The field password cannot be null")
    @Size(min = 6, max = 254, message = "Password address must be between 6 and 254 characters long")
    private String password;

    private Role role;
}
