package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.UserNotFoundException;
import ru.practicum.shareit.user.dao.UserStorage;

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

    public User updateUser(User user) {
        return userStorage.updateUser(user).orElseThrow(
                () -> new UserNotFoundException(String.format("User with id=%s absent", user.getId()))
        );
    }

    public User getUserById(Integer id) {
        return userStorage.getUserById(id)
                .orElseThrow(() -> new UserNotFoundException(MessageFormat.format("User with id={0} not found", id)));
    }
}
