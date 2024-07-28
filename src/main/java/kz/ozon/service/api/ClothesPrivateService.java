package kz.ozon.service.api;

import kz.ozon.dto.product.ClothesDto;
import kz.ozon.dto.product.NewClothesDto;

public interface ClothesPrivateService {
    ClothesDto addClothesDto(Long userId, Long categoryId, NewClothesDto newClothesDto);

    ClothesDto updateClothesDto(Long clothesId, NewClothesDto newClothesDto);

    ClothesDto findClothesDto(Long clothesId);
}