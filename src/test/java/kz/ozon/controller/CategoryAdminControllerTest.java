package kz.ozon.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import kz.ozon.dto.category.CategoryDto;
import kz.ozon.service.api.CategoryAdminService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@WebMvcTest(CategoryAdminController.class)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class CategoryAdminControllerTest {

    @MockBean
    private CategoryAdminService categoryAdminService;

    private final MockMvc mockMvc;
    private final ObjectMapper objectMapper;

    private static final CategoryDto CATEGORY_DTO = CategoryDto.builder()
            .id(1L)
            .name("product for adults")
            .description("Toy")
            .build();

    private static final String BEGINNING_PATH = "/admin/categories";
    private static final String CAT_ID = "/{catId}";

    @Test
    @SneakyThrows
    void addCategoryDto() {
        given(categoryAdminService.addCategoryDto(any(CategoryDto.class))).willReturn(CATEGORY_DTO);

        mockMvc.perform(MockMvcRequestBuilders.post(BEGINNING_PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(CATEGORY_DTO)))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1L))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value(CATEGORY_DTO.getName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.description").value(CATEGORY_DTO.getDescription()));
    }

    @Test
    @SneakyThrows
    void updateCategoryDto() {
        given(categoryAdminService.updateCategory(Mockito.anyLong(), any(CategoryDto.class)))
                .willReturn(CATEGORY_DTO);

        mockMvc.perform(MockMvcRequestBuilders.patch(BEGINNING_PATH + CAT_ID, 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(CATEGORY_DTO)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1L))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value(CATEGORY_DTO.getName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.description").value(CATEGORY_DTO.getDescription()));
    }

    @Test
    @SneakyThrows
    void deleteCategoryDto() {
        mockMvc.perform(MockMvcRequestBuilders.delete(BEGINNING_PATH + CAT_ID, 1L))
                .andExpect(MockMvcResultMatchers.status().isNoContent());
    }
}