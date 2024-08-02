package kz.ozon.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import static kz.ozon.util.Marker.OnCreate;
import static kz.ozon.util.Marker.OnUpdate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class NewUserDto {

    @NotBlank(groups = {OnCreate.class, OnUpdate.class})
    @NotNull(message = "The field name must not be null", groups = OnCreate.class)
    @Size(min = 2, max = 250, message = "Name must be between 2 and 250 characters long", groups = {OnCreate.class, OnUpdate.class})
    private String username;

    @Email(message = "Invalid email address format", groups = {OnCreate.class, OnUpdate.class})
    @NotEmpty(message = "Email address cannot be empty", groups = {OnCreate.class, OnUpdate.class})
    @Size(min = 6, max = 254, message = "Email address must be between 6 and 254 characters long")
    private String email;

    @NotNull(message = "The field password cannot be null", groups = OnCreate.class)
    @Size(min = 6, max = 254, message = "Password address must be between 6 and 254 characters long", groups = {OnCreate.class, OnUpdate.class})
    private String password;

    private RoleDto roleDto;
}