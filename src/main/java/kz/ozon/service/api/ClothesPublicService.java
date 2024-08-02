package kz.ozon.service.api;

import kz.ozon.dto.product.ClothesDto;
import kz.ozon.enums.ClothesSold;
import kz.ozon.enums.ClothingSize;
import kz.ozon.enums.Gender;
import kz.ozon.enums.SortClothes;

import java.util.List;

public interface ClothesPublicService {
    List<ClothesDto> findAllByrequest(String text, List<Long> users,
                                      List<Long> categoryIds, SortClothes sortClothes, ClothesSold clothesSold, List<String> colors,
                                      List<ClothingSize> clothingSizes, String material, Gender gender, int from, int size);
}
