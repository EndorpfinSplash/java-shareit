package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.UserNotFoundException;
import ru.practicum.shareit.user.dao.UserRepository;
import ru.practicum.shareit.user.dao.UserStorage;
import ru.practicum.shareit.user.dto.UserCreationDTO;
import ru.practicum.shareit.user.dto.UserOutputDto;
import ru.practicum.shareit.user.dto.UserUpdateDto;

import java.text.MessageFormat;
import java.util.Collection;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {


    private final UserStorage userStorage;
//    private final UserRepository userStorage;

    public Collection<UserOutputDto> getAllUsers() {
        return userStorage.findAll().stream()
                .map(UserMapper::toUserOutputDto)
                .collect(Collectors.toList());
    }

    public UserOutputDto createUser(UserCreationDTO userCreationDTO) {
        User user = UserMapper.toUser(userCreationDTO);
        User savedUser = userStorage.save(user);
        return UserMapper.toUserOutputDto(savedUser);
    }

    public UserOutputDto updateUser(Integer userId, UserUpdateDto userUpdateDto) {
        User userForUpdate = userStorage.findById(userId).orElseThrow(
                () -> new UserNotFoundException(MessageFormat.format("User with userId={0} not found", userId)));
        User editedUser = UserMapper.toUser(userForUpdate, userUpdateDto);

        User updatedUser = userStorage.update(userId, editedUser).orElseThrow(
                () -> new UserNotFoundException(String.format("User with id=%s absent", userId))
        );

        return UserMapper.toUserOutputDto(updatedUser);
    }

    public UserOutputDto getUserById(Integer userId) {
        User user = userStorage.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(MessageFormat.format("User with userId={0} not found", userId)));
        return UserMapper.toUserOutputDto(user);
    }

    public void deleteUserById(Integer id) {
        userStorage.deleteById(id);
    }
}
