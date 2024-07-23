package kz.ozon.service.api;

import kz.ozon.dto.category.CategoryDto;

import java.util.List;

public interface CategoryPublicService {
    List<CategoryDto> getCategories(int from, int size);

    CategoryDto getCategoryDto(Long catId);
}
