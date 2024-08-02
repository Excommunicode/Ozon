package kz.ozon.mapper;

import kz.ozon.dto.user.NewUserDto;
import kz.ozon.dto.user.UserDto;
import kz.ozon.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.List;

@Mapper(componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface UserMapper {

    UserDto toUserDto(User user);

    User fromNewUserDto(NewUserDto newUserDto);

    @Mapping(target = "id", ignore = true)
    User updateUserDto(@MappingTarget User user, NewUserDto newUserDto);

    List<UserDto> toUserDtoList(List<User> users);

    NewUserDto fromUserToNewUserDto(User user);
}