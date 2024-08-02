package kz.ozon.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import kz.ozon.dto.category.CategoryDto;
import kz.ozon.service.api.CategoryPublicService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.instancio.Instancio;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;

import static kz.ozon.constant.Constant.INITIAL_X;
import static kz.ozon.constant.Constant.LIMIT;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.eq;
import static org.mockito.BDDMockito.given;

@WebMvcTest(CategoryPublicController.class)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class CategoryPublicControllerTest {
    @MockBean
    private final CategoryPublicService categoryPublicService;

    private final MockMvc mockMvc;
    private final ObjectMapper objectMapper;

    private static final List<CategoryDto> CATEGORIES = Instancio.createList(CategoryDto.class);
    private static final CategoryDto CATEGORY_DTO = Instancio.create(CategoryDto.class);
    private static final String BEGINNING_PATH = "/categories";
    private static final String CAT_ID = "/{catId}";

    @Test
    @SneakyThrows
    void getCategories() {
        given(categoryPublicService.getCategories(eq(0), eq(10)))
                .willReturn(CATEGORIES);
        mockMvc.perform(MockMvcRequestBuilders.get(BEGINNING_PATH )
                .param("from", INITIAL_X)
                .param("from", LIMIT)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(objectMapper.writeValueAsString(CATEGORIES)));
    }
    @Test
    @SneakyThrows
    void getCategoryDto() {
        given(categoryPublicService.getCategoryDto(anyLong())).willReturn(CATEGORY_DTO);
        mockMvc.perform(MockMvcRequestBuilders.get(BEGINNING_PATH + CAT_ID, 1L))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(CATEGORY_DTO.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value(CATEGORY_DTO.getName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.description").value(CATEGORY_DTO.getDescription()));
    }
}