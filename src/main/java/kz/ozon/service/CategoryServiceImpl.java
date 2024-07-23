package kz.ozon.service;

import kz.ozon.dto.category.CategoryDto;
import kz.ozon.exception.DataConflictException;
import kz.ozon.exception.NotFoundException;
import kz.ozon.mapper.CategoryMapper;
import kz.ozon.model.Category;
import kz.ozon.repository.CategoryRepository;
import kz.ozon.service.api.CategoryAdminService;
import kz.ozon.service.api.CategoryPublicService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true, isolation = Isolation.REPEATABLE_READ, propagation = Propagation.REQUIRED)
public class CategoryServiceImpl implements CategoryAdminService, CategoryPublicService {
    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    @Override
    @Transactional
    public CategoryDto addCategoryDto(CategoryDto categoryDto) {
        log.info("Adding category with name: {}", categoryDto.getName());

        checkExistsByName(categoryDto.getName());
        CategoryDto savedCategory = categoryMapper.toCategoryDto(categoryRepository.save(categoryMapper.toCategory(categoryDto)));

        log.info("Category added with id: {}", savedCategory.getId());
        return savedCategory;
    }

    @Override
    @Transactional
    public CategoryDto updateCategory(Long catId, CategoryDto categoryDto) {
        log.info("Updating category with id: {}", catId);
        Category category = categoryRepository.findById(catId).orElseThrow(() -> NotFoundException.builder()
                .message(String.format("Category with id: %s not found", catId))
                .build());

        Category updatedCategory = categoryMapper.updateCategory(category, categoryDto);

        CategoryDto updatedCategoryDto = categoryMapper.toCategoryDto(categoryRepository.save(updatedCategory));
        log.info("Category updated with id: {}", updatedCategoryDto.getId());
        return updatedCategoryDto;
    }

    @Override
    @Transactional
    public void deleteCategory(Long catId) {
        log.info("Deleting category with id: {}", catId);

        categoryRepository.deleteById(catId);

        log.info("Category with id: {} deleted", catId);
    }

    @Override
    public List<CategoryDto> getCategories(int from, int size) {
        log.info("Fetching categories from: {} size: {}", from, size);

        PageRequest pageable = PageRequest.of(from > 0 ? from / size : 0, size, Sort.by(Sort.Direction.DESC, "id"));
        List<CategoryDto> listCategoryDto = categoryMapper.toListCategoryDto(categoryRepository.findAll(pageable).getContent());

        log.info("Fetched {} categories", listCategoryDto.size());
        return !listCategoryDto.isEmpty() ? listCategoryDto : Collections.emptyList();
    }

    @Override
    public CategoryDto getCategoryDto(Long catId) {
        log.info("Fetching category with id: {}", catId);
        return findByIdCategoryDto(catId);
    }

    private CategoryDto findByIdCategoryDto(Long catId) {
        CategoryDto categoryDto = categoryMapper.toCategoryDto(categoryRepository.findById(catId).orElseThrow(() -> NotFoundException.builder()
                .message(String.format("Category with id: %s not found", catId))
                .build()));
        log.info("Found category with id: {}", categoryDto.getId());
        return categoryDto;
    }

    private void checkExistsByName(String name) {

        if (categoryRepository.existsByName(name)) {
            log.warn("Category name already taken: {}", name);
            throw DataConflictException.builder()
                    .message(String.format("This name: %s already be taken", name))
                    .build();
        }
    }
}
