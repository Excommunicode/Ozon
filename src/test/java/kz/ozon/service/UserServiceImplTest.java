package kz.ozon.service;

import jakarta.persistence.EntityManager;
import kz.ozon.dto.user.NewUserDto;
import kz.ozon.dto.user.UserDto;
import kz.ozon.exception.DataConflictException;
import kz.ozon.exception.NotFoundException;
import kz.ozon.mapper.UserMapper;
import kz.ozon.model.User;
import kz.ozon.repository.UserRepository;
import kz.ozon.service.api.UserPrivateService;
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

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@Transactional
@SpringBootTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Sql(scripts = "/schema.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
class UserServiceImplTest {
    private final UserPrivateService userPrivateService;
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final EntityManager entityManager;

    private NewUserDto newUserDto;


    @BeforeEach
    void setUp() {
        User user = Instancio.of(User.class)
                .ignore(Select.field(User::getId))
                .create();

        userMapper.toUserDto(user);
        newUserDto = userMapper.fromUserToNewUserDto(user);
    }

    @AfterEach
    void tearDown() {
        userRepository.deleteAll();
        entityManager.clear();
    }

    @Test
    void addUserDto() {

        UserDto userDto1 = userPrivateService.addUserDto(newUserDto);
        System.err.println(userDto1.getId());
        entityManager.clear();

        UserDto findUserDto = findUserDto(1L);
        assertEquals(userDto1, findUserDto);
    }


    @Test
    void updateUserDto() {
        userPrivateService.addUserDto(newUserDto);

        String s = Instancio.create(String.class);
        newUserDto.setName(s);

        UserDto updateUserDto = userPrivateService.updateUserDto(1L, newUserDto);

        UserDto userDto1 = findUserDto(updateUserDto.getId());
        assertEquals(s, userDto1.getName());

    }

    @Test
    void deleteUserDto() {
        userPrivateService.addUserDto(newUserDto);

        userPrivateService.deleteUserDto(1L);

        assertThrows(NotFoundException.class, () -> findUserDto(1L));
    }

    @Test
    void getUserDto() {
        UserDto addUserDto = userPrivateService.addUserDto(newUserDto);
        UserDto userDto1 = userPrivateService.getUserDto(1L);
        assertEquals(userDto1, addUserDto);
    }

    @Test
    void getUsers_WithNullIds() {
        List<User> users = Stream.generate(() -> Instancio.of(User.class)
                        .ignore(Select.field(User::getId))
                        .create())
                .limit(10)
                .toList();
        List<UserDto> userDtoList = userMapper.toUserDtoList(userRepository.saveAll(users));

        userDtoList.sort(Comparator.comparing(UserDto::getId).reversed());

        List<UserDto> serviceUsers = userPrivateService.getUsers(Collections.emptyList(), 0, 10);

        assertEquals(userDtoList, serviceUsers);
    }

    @Test
    void getUsers_WithIds() {
        List<User> users = Stream.generate(() -> Instancio.of(User.class)
                        .ignore(Select.field(User::getId))
                        .create())
                .limit(10)
                .toList();

        List<UserDto> userDtoList = userMapper.toUserDtoList(userRepository.saveAll(users));
        userDtoList.sort(Comparator.comparing(UserDto::getId).reversed());

        List<Long> longs = userDtoList.stream()
                .map(UserDto::getId)
                .toList();

        List<UserDto> users1 = userPrivateService.getUsers(longs, 0, 10);
        assertEquals(users1, userDtoList);
    }

    @Test
    void addUser_WithExistsEmail() {
        userPrivateService.addUserDto(newUserDto);
        assertThrows(DataConflictException.class, () -> userPrivateService.addUserDto(newUserDto));
    }

    @Test
    void updateUser_WithExistsEmail() {
        String email = "faruh28.06@mail.ru";
        userPrivateService.addUserDto(newUserDto);
        NewUserDto newUserDto1 = Instancio.of(NewUserDto.class)
                .set(Select.field(NewUserDto::getEmail), email)
                .create();
        userPrivateService.addUserDto(newUserDto1);
        newUserDto.setEmail(email);

        assertThrows(DataConflictException.class, () -> userPrivateService.updateUserDto(1L, newUserDto));
    }

    @Test
    void getUser_WithWrongId() {
        userPrivateService.addUserDto(newUserDto);

        assertThrows(NotFoundException.class, () -> userPrivateService.getUserDto(100L));
    }

    @Test
    void updateUser_WithWrongId() {
        assertThrows(NotFoundException.class, () -> userPrivateService.updateUserDto(100L, newUserDto));
    }

    private UserDto findUserDto(Long id) {
        return userMapper.toUserDto(userRepository.findById(id).orElseThrow(() -> NotFoundException.builder()
                .message(String.format("User with id %s not found", id))
                .build()));
    }
}