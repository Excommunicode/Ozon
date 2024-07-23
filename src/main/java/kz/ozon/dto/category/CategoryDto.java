package kz.ozon.dto.category;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class CategoryDto {
    private Long id;
    @NotBlank(message = "The field message cannot be blank")
    @NotNull(message = "The field message cannot be null")
    @Size(max = 50, message = "The max characters 50")
    private String name;
    @Size(max = 1024, message = "The max characters 1024")
    private String description;
}
