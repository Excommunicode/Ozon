package kz.ozon.service;

import kz.ozon.dto.product.ClothesDto;
import kz.ozon.dto.product.NewClothesDto;
import kz.ozon.exception.NotFoundException;
import kz.ozon.mapper.ClothesMapper;
import kz.ozon.model.Clothes;
import kz.ozon.repository.CategoryRepository;
import kz.ozon.repository.ClothesRepository;
import kz.ozon.repository.UserRepository;
import kz.ozon.service.api.ClothesPrivateService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true, isolation = Isolation.REPEATABLE_READ, propagation = Propagation.REQUIRED)
public class ClothesPrivateServiceImpl implements ClothesPrivateService {
    private final ClothesRepository clothesRepository;
    private final ClothesMapper clothesMapper;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;


    @Override
    @Transactional
    public ClothesDto addClothesDto(Long userId, Long categoryId, NewClothesDto newClothesDto) {
        log.debug("Adding clothes for userId: {}, categoryId: {}, clothesDto: {}", userId, categoryId, newClothesDto);

        existsUserById(userId);
        existsCategoryById(categoryId);

        ClothesDto savedClothesDto = clothesMapper.toClosesDto(
                clothesRepository.save(clothesMapper.toClothes(userId, categoryId, newClothesDto, LocalDateTime.now()))
        );

        log.info("Successfully added clothes: {}", savedClothesDto);
        return savedClothesDto;
    }

    @Override
    @Transactional
    public ClothesDto updateClothesDto(Long clothesId, NewClothesDto newClothesDto) {
        Clothes clothes = findClothesById(clothesId);

        Clothes updateClothes = clothesMapper.updateClothes(clothes, newClothesDto, LocalDateTime.now());

        return clothesMapper.toClosesDto(updateClothes);
    }

    @Override
    public ClothesDto findClothesDto(Long clothesId) {
        return findByIdClothesDto(clothesId);
    }

    private ClothesDto findByIdClothesDto(Long clothesId) {

        return clothesMapper.toClosesDto(findClothesById(clothesId));
    }

    private Clothes findClothesById(Long clothesId) {

        return clothesRepository.findById(clothesId).orElseThrow(() -> NotFoundException.builder()
                .message(String.format("Clothes with id: %s not found", clothesId))
                .build());
    }

    private void existsUserById(Long userId) {

        if (!userRepository.existsById(userId)) {
            throw NotFoundException.builder()
                    .message(String.format("User with id: %s not found", userId))
                    .build();
        }
    }

    private void existsCategoryById(Long categoryId) {

        if (!categoryRepository.existsById(categoryId)) {
            throw NotFoundException.builder()
                    .message(String.format("Category with id: %s not found", categoryId))
                    .build();
        }
    }
}
