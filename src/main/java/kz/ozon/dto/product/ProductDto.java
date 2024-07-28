package kz.ozon.dto.product;

import kz.ozon.dto.category.CategoryDto;
import kz.ozon.dto.user.UserDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = false)
public abstract class ProductDto {

    private Long id;

    private String name;

    private String description;

    private double price;

    private CategoryDto category;

    private UserDto owner;

    private String createdAt;

    private String updatedAt;

    private boolean isSold = false;
}
