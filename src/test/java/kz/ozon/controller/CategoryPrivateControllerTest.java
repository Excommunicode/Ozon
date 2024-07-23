package kz.ozon.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import kz.ozon.service.api.CategoryAdminService;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class CategoryPrivateControllerTest {
    @MockBean
    private CategoryAdminService categoryAdminService;

    private final MockMvc mockMvc;
    private final ObjectMapper objectMapper;

    @Test
    void addCategoryDto() {
    }

    @Test
    void updateCategoryDto() {
    }

    @Test
    void deleteCategoryDto() {
    }
}