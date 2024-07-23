package kz.ozon.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import kz.ozon.dto.user.NewUserDto;
import kz.ozon.dto.user.UserDto;
import kz.ozon.enums.Role;
import kz.ozon.service.api.UserPrivateService;
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
import java.util.stream.Collectors;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;

@WebMvcTest(UserPrivateController.class)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class UserPrivateControllerTest {
    @MockBean
    private UserPrivateService userPrivateService;

    private final MockMvc mockMvc;
    private final ObjectMapper objectMapper;

    private static final NewUserDto NEW_USER_DTO = NewUserDto.builder()
            .name("John Doe")
            .email("johndoe@example.com")
            .password("securePassword123")
            .role(Role.USER)
            .build();

    private static final UserDto USER_DTO = UserDto.builder()
            .id(1L)
            .name(NEW_USER_DTO.getName())
            .email(NEW_USER_DTO.getEmail())
            .build();
    private static final String PATH = "/admin/users";

    @Test
    @SneakyThrows
    void saveUserDto() {
        given(userPrivateService.addUserDto(eq(NEW_USER_DTO)))
                .willReturn(USER_DTO);

        mockMvc.perform(MockMvcRequestBuilders.post(PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(NEW_USER_DTO)))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(USER_DTO.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value(USER_DTO.getName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.email").value(USER_DTO.getEmail()));
    }

    @Test
    @SneakyThrows
    void updateUserDto() {
        given(userPrivateService.updateUserDto(anyLong(), eq(NEW_USER_DTO)))
                .willReturn(USER_DTO);

        mockMvc.perform(MockMvcRequestBuilders.patch(PATH + "/{userId}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(NEW_USER_DTO)))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @SneakyThrows
    void deleteUserDto() {
        mockMvc.perform(MockMvcRequestBuilders.delete(PATH + "/{userId}", 1L))
                .andExpect(MockMvcResultMatchers.status().isNoContent());
    }

    @Test
    @SneakyThrows
    void getUSER_DTO() {
        given(userPrivateService.getUserDto(1L)).willReturn(USER_DTO);

        mockMvc.perform(MockMvcRequestBuilders.get(PATH + "/{userId}", 1L))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value(USER_DTO.getName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.email").value(USER_DTO.getEmail()));
    }

    @Test
    @SneakyThrows
    void getUsers() {
        final String PATH = "/admin/users";

        List<UserDto> users = Instancio.createList(UserDto.class);
        List<Long> ids = users.stream()
                .map(UserDto::getId)
                .collect(Collectors.toList());
        when(userPrivateService.getUsers(ids, 0, 10)).thenReturn(users);

        mockMvc.perform(MockMvcRequestBuilders.get(PATH)
                        .param("ids", "1", "2", "3")
                        .param("from", "0")
                        .param("size", "10"))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }
}