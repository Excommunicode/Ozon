package kz.ozon.service;

import jakarta.persistence.EntityManager;
import kz.ozon.base.BaseTest;
import kz.ozon.dto.category.CategoryDto;
import kz.ozon.exception.DataConflictException;
import kz.ozon.exception.NotFoundException;
import kz.ozon.mapper.CategoryMapper;
import kz.ozon.model.Category;
import kz.ozon.repository.CategoryRepository;
import kz.ozon.service.api.CategoryAdminService;
import kz.ozon.service.api.CategoryPublicService;
import lombok.RequiredArgsConstructor;
import org.instancio.Instancio;
import org.instancio.Select;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;


@RequiredArgsConstructor(onConstructor_ = @Autowired)
class CategoryServiceImplTest extends BaseTest {
    private final CategoryAdminService categoryAdminService;
    private final CategoryPublicService categoryPublicService;
    private final CategoryMapper categoryMapper;
    private final CategoryRepository categoryRepository;
    private final EntityManager entityManager;

    private CategoryDto categoryDto;

    @BeforeEach
    void setUp() {
        Category category = Instancio.of(Category.class)
                .ignore(Select.field(Category::getId))
                .create();
        categoryDto = categoryMapper.toCategoryDto(category);
    }

    @AfterEach
    void tearDown() {
        categoryRepository.deleteAll();
        entityManager.clear();
    }

    @Test
    void addCategoryDto() {
        CategoryDto categoryDto1 = categoryAdminService.addCategoryDto(categoryDto);

        CategoryDto categoryDto2 = getCategoryDto(1L);
        assertEquals(categoryDto1, categoryDto2);
    }

    @Test
    void addCategoryDtoWithExistsName() {
        categoryAdminService.addCategoryDto(categoryDto);

        assertThrows(DataConflictException.class, () -> categoryAdminService.addCategoryDto(categoryDto));
    }


    @Test
    void updateCategory() {
        CategoryDto addedCategoryDto = categoryAdminService.addCategoryDto(categoryDto);

        String name = Instancio.create(String.class);
        String description = Instancio.create(String.class);
        addedCategoryDto.setName(name);
        addedCategoryDto.setDescription(description);

        categoryAdminService.updateCategory(1L, addedCategoryDto);

        CategoryDto categoryDto1 = getCategoryDto(1L);

        assertEquals(addedCategoryDto, categoryDto1);
    }

    @Test
    void deleteCategory() {
        categoryAdminService.addCategoryDto(categoryDto);

        categoryAdminService.deleteCategory(1L);

        assertThrows(NotFoundException.class, () -> getCategoryDto(1L));
    }

    @Test
    void getCategories() {
        List<Category> categories = Stream.generate(() -> Instancio.of(Category.class)
                        .ignore(Select.field(Category::getId))
                        .create())
                .limit(10)
                .toList();
        categoryRepository.saveAll(categories);

        List<CategoryDto> categoryDtoList = categoryPublicService.getCategories(0, 10);
        List<CategoryDto> listCategoryDto = categoryMapper.toListCategoryDto(categories);
        listCategoryDto.sort(Comparator.comparing(CategoryDto::getId).reversed());
        assertEquals(categoryDtoList, listCategoryDto);

    }

    @Test
    void getCategories_EmptyList() {
        List<CategoryDto> categoryDtoList = categoryPublicService.getCategories(0, 10);
        assertEquals(categoryDtoList.size(), 0);
        assertEquals(categoryDtoList, Collections.emptyList());
    }

    @Test
    void getCategoryDto() {
        CategoryDto categoryDto1 = categoryAdminService.addCategoryDto(categoryDto);
        assertEquals(categoryPublicService.getCategoryDto(1L), categoryDto1);
    }

    @Test
    void getCategoryDto_WithNoId() {
        categoryAdminService.addCategoryDto(categoryDto);
        assertThrows(NotFoundException.class, () -> categoryPublicService.getCategoryDto(1000L));
    }

    private CategoryDto getCategoryDto(Long id) {
        return categoryMapper.toCategoryDto(categoryRepository.findById(id).orElseThrow(() -> NotFoundException.builder()
                .message(String.format("Category with id: %s not found", id))
                .build()));
    }
}