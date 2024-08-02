package kz.ozon.mapper;

import kz.ozon.dto.category.CategoryDto;
import kz.ozon.model.Category;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.List;

@Mapper(componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface CategoryMapper {
    Category toCategory(CategoryDto categoryDto);

    CategoryDto toCategoryDto(Category category);

    @Mapping(target = "id", ignore = true)
    Category updateCategory(@MappingTarget Category category, CategoryDto categoryDto);

    List<CategoryDto> toListCategoryDto(List<Category> categories);
}