package kz.ozon.service.api;

import kz.ozon.dto.category.CategoryDto;

public interface CategoryAdminService {
    CategoryDto addCategoryDto(CategoryDto categoryDto);

    CategoryDto updateCategory(Long catId, CategoryDto categoryDto);

    void deleteCategory(Long catId);
}
