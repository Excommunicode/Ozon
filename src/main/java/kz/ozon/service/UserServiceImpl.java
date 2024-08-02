package kz.ozon.service;

import kz.ozon.dto.user.NewUserDto;
import kz.ozon.dto.user.UserDto;
import kz.ozon.exception.DataConflictException;
import kz.ozon.exception.NotFoundException;
import kz.ozon.mapper.UserMapper;
import kz.ozon.model.User;
import kz.ozon.repository.UserRepository;
import kz.ozon.service.api.UserPrivateService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true, isolation = Isolation.REPEATABLE_READ, propagation = Propagation.REQUIRED)
public class UserServiceImpl implements UserPrivateService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public UserDto addUserDto(NewUserDto newUserDto) {
        log.debug("Adding new user DTO: {}", newUserDto);

        checkExistsEmail(newUserDto.getEmail());
        existsName(newUserDto.getUsername());

        User user = userMapper.fromNewUserDto(newUserDto);
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        UserDto savedUserDto = userMapper.toUserDto(userRepository.save(user));

        log.info("Added new user DTO with ID: {}", savedUserDto.getId());
        return savedUserDto;
    }

    @Override
    @Transactional
    public UserDto updateUserDto(Long userId, NewUserDto newUserDto) {
        log.debug("Updating user DTO with ID: {}", userId);

        User userFromDb = findUserById(userId);

        checkEmailOnUpdate(newUserDto.getEmail(), userFromDb.getEmail());
        existsNameWhenUpdate(newUserDto.getUsername(), userFromDb.getUsername());

        User updated = userMapper.updateUserDto(userFromDb, newUserDto);

        UserDto result = userMapper.toUserDto(userRepository.save(updated));

        log.info("Updated user DTO with ID: {}", result.getId());
        return result;
    }

    @Override
    @Transactional
    public void deleteUserDto(Long userId) {
        log.debug("Deleting user DTO with ID: {}", userId);

        userRepository.deleteById(userId);

        log.info("Deleted user DTO with ID: {}", userId);
    }

    @Override
    public UserDto getUserDto(Long userId) {
        log.debug("Retrieving user DTO with ID: {}", userId);

        UserDto result = findUserDtoById(userId);

        log.info("Retrieved user DTO: {}", result);
        return result;
    }

    @Override
    public List<UserDto> getUsers(List<Long> userIds, int from, int size) {
        log.info("Fetching user DTOs, Page: {}, Size: {}", from, size);

        PageRequest pageable = PageRequest.of(from > 0 ? from / size : 0, size, Sort.by(Sort.Direction.DESC, "id"));
        List<UserDto> userDtoList = !userIds.isEmpty() ? findAllById(userIds, pageable) : findAllUsersDto(pageable);

        log.info("Fetched {} user DTOs.", userDtoList.size());
        return userDtoList;
    }

    private void checkEmailOnUpdate(String newUserEmail, String oldEmail) {
        if (!Objects.equals(newUserEmail, oldEmail)) {
            checkExistsEmail(newUserEmail);
        }
    }

    private void checkExistsEmail(String email) {
        if (userRepository.existsByEmail(email)) {
            throw DataConflictException.builder()
                    .message(String.format("This email: %s already be taken", email))
                    .build();
        }
    }

    private List<UserDto> findAllById(List<Long> userIds, PageRequest pageable) {
        log.debug("Finding user DTOs by IDs: {}", userIds);
        List<UserDto> userDtoList = userMapper.toUserDtoList(userRepository.findAllByIdIn(userIds, pageable));
        log.debug("Found {} user DTOs for provided IDs.", userDtoList.size());
        return userDtoList;
    }

    private List<UserDto> findAllUsersDto(PageRequest pageable) {
        log.debug("Fetching all user DTOs with pagination.");
        List<UserDto> userDtoList = userMapper.toUserDtoList(userRepository.findAll(pageable).getContent());
        log.debug("Fetched {} user DTOs.", userDtoList.size());
        return userDtoList;
    }

    private UserDto findUserDtoById(Long userId) {
        return userMapper.toUserDto(findUserById(userId));
    }

    private User findUserById(Long userId) {
        return userRepository.findById(userId).orElseThrow(() -> NotFoundException.builder()
                .message(String.format("User with id: %s not found", userId))
                .build());
    }

    private void existsName(String username) {
        if (userRepository.existsByUsername(username)) {
            throw DataConflictException.builder()
                    .message(String.format("User with username: %s already exists", username))
                    .build();
        }
    }

    private void existsNameWhenUpdate(String newUsername, String oldUsername) {
        if (!Objects.equals(newUsername, oldUsername)) {
            existsName(newUsername);
        }
    }
}