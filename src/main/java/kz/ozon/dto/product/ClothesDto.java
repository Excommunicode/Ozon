package kz.ozon.dto.product;

import kz.ozon.enums.ClothingSize;
import kz.ozon.enums.Gender;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = true)
public class ClothesDto extends ProductDto {
    private String color;
    private ClothingSize size;
    private String material;
    private Gender gender;
}
