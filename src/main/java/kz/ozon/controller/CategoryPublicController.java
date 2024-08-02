package kz.ozon.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import kz.ozon.dto.category.CategoryDto;
import kz.ozon.service.api.CategoryPublicService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static kz.ozon.constant.Constant.INITIAL_X;
import static kz.ozon.constant.Constant.LIMIT;

@RestController
@RequiredArgsConstructor
@RequestMapping("/categories")
@Tag(name = "Public: Категории", description = "Публичный Api для работы с категориями")
public class CategoryPublicController {
    private final CategoryPublicService categoryPublicService;

    @GetMapping
    public List<CategoryDto> getCategories(@PositiveOrZero @RequestParam(defaultValue = INITIAL_X) int from,
                                           @Positive @RequestParam(defaultValue = LIMIT) int size) {
        return categoryPublicService.getCategories(from, size);
    }

    @GetMapping("/{catId}")
    public CategoryDto getCategoryDto(@PathVariable Long catId) {
        return categoryPublicService.getCategoryDto(catId);
    }
}
