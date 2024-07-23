package kz.ozon.service.api;

import kz.ozon.dto.user.NewUserDto;
import kz.ozon.dto.user.UserDto;

import java.util.List;

public interface UserPrivateService {
    UserDto addUserDto(NewUserDto newUserDto);

    UserDto updateUserDto(Long userId, NewUserDto newUserDto);

    void deleteUserDto(Long userId);

    UserDto getUserDto(Long userId);

    List<UserDto> getUsers(List<Long> users, int from, int size);
}