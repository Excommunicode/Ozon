package kz.ozon.mapper;

import kz.ozon.dto.product.ClothesDto;
import kz.ozon.dto.product.NewClothesDto;
import kz.ozon.model.Clothes;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Mappings;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.time.LocalDateTime;

import static kz.ozon.constant.Constant.DATA_TIME_FORMATTER;

@Mapper(componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface ClothesMapper {

    @Mappings({
            @Mapping(target = "owner.id", source = "ownerId"),
            @Mapping(target = "category.id", source = "categoryId"),
            @Mapping(target = "createdAt", source = "now", dateFormat = DATA_TIME_FORMATTER)
    })
    Clothes toClothes(Long ownerId, Long categoryId, NewClothesDto newClothesDto, LocalDateTime now);

    @Mappings({
            @Mapping(target = "createdAt", source = "createdAt", dateFormat = DATA_TIME_FORMATTER),
            @Mapping(target = "updatedAt", source = "updatedAt", dateFormat = DATA_TIME_FORMATTER)
    })
    ClothesDto toClosesDto(Clothes clothes);

    NewClothesDto toNewClosesDtoForTest(Clothes clothes);

    @Mappings({
            @Mapping(target = "clothes.createdAt", ignore = true),
            @Mapping(target = "clothes.updatedAt", source = "now", dateFormat = DATA_TIME_FORMATTER)
    })
    Clothes updateClothes(@MappingTarget Clothes clothes, NewClothesDto newClothesDto, LocalDateTime now);
}