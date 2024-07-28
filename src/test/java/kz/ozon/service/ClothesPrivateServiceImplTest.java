package kz.ozon.service;

import jakarta.persistence.EntityManager;
import kz.ozon.dto.product.ClothesDto;
import kz.ozon.dto.product.NewClothesDto;
import kz.ozon.mapper.ClothesMapper;
import kz.ozon.model.Category;
import kz.ozon.model.Clothes;
import kz.ozon.model.User;
import kz.ozon.repository.CategoryRepository;
import kz.ozon.repository.ClothesRepository;
import kz.ozon.repository.UserRepository;
import kz.ozon.service.api.ClothesPrivateService;
import lombok.RequiredArgsConstructor;
import org.instancio.Instancio;
import org.instancio.Select;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;


import java.math.BigDecimal;
import java.math.RoundingMode;

import static org.junit.jupiter.api.Assertions.*;

@Transactional
@SpringBootTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Sql(scripts = "/schema.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
class ClothesPrivateServiceImplTest {
    private final ClothesPrivateService clothesPrivateService;
    private final ClothesRepository clothesRepository;
    private final ClothesMapper clothesMapper;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final EntityManager entityManager;

    private User user;
    private Category category;
    private ClothesDto clothesDto;
    private NewClothesDto newClothesDto;

    @BeforeEach
    void setUp() {
        user = Instancio.of(User.class)
                .ignore(Select.field(User::getId))
                .create();
        User addedUser = userRepository.save(user);
        category = Instancio.of(Category.class)
                .ignore(Select.field(Category::getId))
                .create();
        Category addedCategory = categoryRepository.save(category);
        Clothes clothes = Instancio.of(Clothes.class)
                .ignore(Select.field(Clothes::getId))
                .supply(Select.field(Clothes::getOwner), () -> addedUser)
                .supply(Select.field(Clothes::getCategory), () -> addedCategory)
                .ignore(Select.field(Clothes::getUpdatedAt))
                .ignore(Select.field(Clothes::getCreatedAt))
                .create();
        clothesDto = clothesMapper.toClosesDto(clothes);
        newClothesDto = clothesMapper.toNewClosesDtoForTest(clothes);
    }

    @AfterEach
    void tearDown() {
        clothesRepository.deleteAll();
        entityManager.clear();
    }

    @Test
    void addClothesDto() {
        ClothesDto addClothesDto = clothesPrivateService.addClothesDto(user.getId(), category.getId(), newClothesDto);

        entityManager.clear();
        ClothesDto clothesDtoFromDb = clothesMapper.toClosesDto(clothesRepository.findById(1L).orElse(null));


        assertEquals(addClothesDto.getId(), clothesDtoFromDb.getId(), "ID fields are not equal");
        assertEquals(addClothesDto.getName(), clothesDtoFromDb.getName(), "Name fields are not equal");
        assertEquals(addClothesDto.getCategory().getId(), clothesDtoFromDb.getCategory().getId(), "Category fields are not equal");
        assertEquals(addClothesDto.getOwner().getId(), clothesDtoFromDb.getOwner().getId(), "UserId fields are not equal");
        assertEquals(addClothesDto.getCreatedAt(), clothesDtoFromDb.getCreatedAt(), "CreatedAt fields are not equal");

        BigDecimal price1 = BigDecimal.valueOf(addClothesDto.getPrice()).setScale(2, RoundingMode.HALF_UP);
        BigDecimal price2 = BigDecimal.valueOf(clothesDtoFromDb.getPrice()).setScale(2, RoundingMode.HALF_UP);

        assertEquals(price1, price2);
    }

    @Test
    void updateClothesDto() {
        ClothesDto addClothesDto = clothesPrivateService.addClothesDto(user.getId(), category.getId(), newClothesDto);
        NewClothesDto toUpdateClothesDto = Instancio.create(NewClothesDto.class);

        ClothesDto updateClothesDto = clothesPrivateService.updateClothesDto(addClothesDto.getId(), toUpdateClothesDto);
        ClothesDto retrievedClothesDto = clothesMapper.toClosesDto(clothesRepository.findById(updateClothesDto.getId()).orElse(null));

        assertNotNull(retrievedClothesDto, "Retrieved ClothesDto should not be null");
        assertEquals(updateClothesDto.getId(), retrievedClothesDto.getId(), "IDs should match");
        assertEquals(toUpdateClothesDto.getName(), retrievedClothesDto.getName(), "Names should match");
        assertEquals(toUpdateClothesDto.getDescription(), retrievedClothesDto.getDescription(), "Descriptions should match");
        assertEquals(toUpdateClothesDto.getPrice(), retrievedClothesDto.getPrice(), "Prices should match");


        assertNotEquals(addClothesDto.getName(), retrievedClothesDto.getName(), "Names should be updated");
        assertNotEquals(addClothesDto.getDescription(), retrievedClothesDto.getDescription(), "Descriptions should be updated");
        assertNotEquals(addClothesDto.getPrice(), retrievedClothesDto.getPrice(), "Prices should be updated");
    }



}