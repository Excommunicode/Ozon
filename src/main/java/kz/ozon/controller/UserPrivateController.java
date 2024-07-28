package kz.ozon.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import kz.ozon.dto.user.NewUserDto;
import kz.ozon.dto.user.UserDto;
import kz.ozon.exception.ApiError;
import kz.ozon.service.api.UserPrivateService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static kz.ozon.constant.Constant.INITIAL_X;
import static kz.ozon.constant.Constant.LIMIT;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/admin/users")
@Tag(name = "Admin: Пользователи", description = "Api для работы с пользователями")
public class UserPrivateController {
    private final UserPrivateService userService;

    @Operation(summary = "Добавление нового пользователя",
            description = "Добавляет пользователя в базу данных")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Пользователь успешно добавлен",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = NewUserDto.class))),
            @ApiResponse(responseCode = "400", description = "Некорректные данные (например, невалидные форматы данных, отсутствующие обязательные поля и т.п.)",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiError.class))),
            @ApiResponse(responseCode = "409", description = "Нарушение целостности данных (например, дублирование уникального значения)",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiError.class)))
    })
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserDto saveUserDto(@Valid @RequestBody NewUserDto newUserDto) {
        return userService.addUserDto(newUserDto);
    }

    @Operation(summary = "Обновление пользователя", description = "Обновляет данные пользователя по идентификатору.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Пользователь успешно обновлен", content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = UserDto.class))),
            @ApiResponse(responseCode = "400", description = "Некорректные данные", content = @Content),
            @ApiResponse(responseCode = "404", description = "Пользователь не найден", content = @Content),
            @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера", content = @Content)
    })
    @PatchMapping("/{userId}")
    public UserDto updateUserDto(@PathVariable Long userId, @Valid @RequestBody NewUserDto newUserDto) {
        return userService.updateUserDto(userId, newUserDto);
    }

    @Operation(summary = "Удаление пользователя", description = "Удаляет пользователя по идентификатору.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Пользователь успешно удален"),
            @ApiResponse(responseCode = "404", description = "Пользователь не найден", content = @Content),
            @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера", content = @Content)
    })
    @DeleteMapping("/{userId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUserDto(@PathVariable Long userId) {
        userService.deleteUserDto(userId);
    }

    @Operation(summary = "Получение данных пользователя", description = "Возвращает данные пользователя по его идентификатору.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Данные пользователя получены", content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserDto.class))),
            @ApiResponse(responseCode = "404", description = "Пользователь не найден", content = @Content),
            @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера", content = @Content)
    })
    @GetMapping("/{userId}")
    public UserDto getUserDto(@PathVariable Long userId) {
        return userService.getUserDto(userId);
    }

    @Operation(summary = "Получение списка пользователей", description = "Возвращает список пользователей. Поддерживает пагинацию и фильтрацию по ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Список пользователей получен", content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserDto.class))),
            @ApiResponse(responseCode = "400", description = "Некорректные параметры запроса", content = @Content),
            @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера", content = @Content)
    })
    @GetMapping
    public List<UserDto> getUsers(@RequestParam(value = "ids", required = false) List<Long> ids,
                                  @PositiveOrZero @RequestParam(defaultValue = INITIAL_X) int from,
                                  @Positive @RequestParam(defaultValue = LIMIT) int size) {
        return userService.getUsers(ids, from, size);
    }
}