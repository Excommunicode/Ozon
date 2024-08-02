package kz.ozon.dto.user;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class UserDto {
    @Schema(description = "Идентификатор пользователя")
    private Long id;
    @Schema(description = "Имя пользователя")
    private String username;
    @Schema(description = "Почто пользователя")
    private String email;
}