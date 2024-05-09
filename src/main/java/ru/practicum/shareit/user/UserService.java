package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.UserNotFoundException;
import ru.practicum.shareit.user.dao.UserStorage;
import ru.practicum.shareit.user.dto.UserDto;

import java.text.MessageFormat;
import java.util.Collection;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserStorage userStorage;

    public Collection<User> getAllUsers() {
        return userStorage.getAllUsers();
    }

    public User createUser(User user) {
        return userStorage.saveUser(user);
    }

    public User updateUser(Integer userId, UserDto userDto) {
        User userForUpdate = userStorage.getUserById(userId).orElseThrow(
                () -> new UserNotFoundException(MessageFormat.format("User with userId={0} not found", userId)));

        if (userDto.getEmail() != null) {
            userStorage.checkEmailUniqueness(userForUpdate.getId(), userDto.getEmail());
            userForUpdate.setEmail(userDto.getEmail());
        }
        if (userDto.getName() != null) {
            userForUpdate.setName(userDto.getName());
        }

        return userStorage.updateUser(userId, userForUpdate).orElseThrow(
                () -> new UserNotFoundException(String.format("User with id=%s absent", userId))
        );
    }

    public User getUserById(Integer userId) {
        return userStorage.getUserById(userId)
                .orElseThrow(() -> new UserNotFoundException(MessageFormat.format("User with userId={0} not found", userId)));
    }

    public void deleteUserById(Integer id) {
        userStorage.deleteUserById(id);
    }
}
