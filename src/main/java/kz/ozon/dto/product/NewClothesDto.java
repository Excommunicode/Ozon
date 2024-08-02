package kz.ozon.dto.product;

import kz.ozon.enums.ClothingSize;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class NewClothesDto {
    private String username;
    private String description;
    private Double price;
    private String color;
    private ClothingSize size;
    private String material;
}