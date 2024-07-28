package kz.ozon.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import kz.ozon.dto.product.ClothesDto;
import kz.ozon.dto.product.NewClothesDto;
import kz.ozon.service.api.ClothesPrivateService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/users")
@Tag(name = "Private: Одежда", description = "Приватный Api для работы с одеждой")
public class ClothesPrivateController {
    private final ClothesPrivateService clothesPrivateService;

    @PostMapping("/{userId}/categories/{categoryId}/clothes")
    public ClothesDto addClothesDto(@PathVariable Long userId, @PathVariable Long categoryId, @Valid @RequestBody NewClothesDto newClothesDto) {
        return clothesPrivateService.addClothesDto(userId, categoryId, newClothesDto);
    }

    @PatchMapping("{clothesId}")
    public ClothesDto updateClothesDto(@PathVariable Long clothesId, @Valid @RequestBody NewClothesDto newClothesDto) {
        return clothesPrivateService.updateClothesDto(clothesId, newClothesDto);
    }

    @GetMapping("/{clothesId}")
    public ClothesDto findClothesDto(@PathVariable Long clothesId) {
        return clothesPrivateService.findClothesDto(clothesId);
    }
}