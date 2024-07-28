package kz.ozon.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import kz.ozon.dto.category.CategoryDto;
import kz.ozon.service.api.CategoryAdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/admin/categories")
@Tag(name = "Admin: Категории", description = "Api для работы с категориями")
public class CategoryAdminController {
    private final CategoryAdminService categoryAdminService;

    @Operation(summary = "Добавление новог")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CategoryDto addCategoryDto(@Valid @RequestBody CategoryDto categoryDto) {
        return categoryAdminService.addCategoryDto(categoryDto);
    }

    @PatchMapping("/{catId}")
    public CategoryDto updateCategoryDto(@PathVariable Long catId, @Valid @RequestBody CategoryDto categoryDto) {
        return categoryAdminService.updateCategory(catId, categoryDto);
    }

    @DeleteMapping("/{catId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCategoryDto(@PathVariable Long catId) {
        categoryAdminService.deleteCategory(catId);
    }
}
