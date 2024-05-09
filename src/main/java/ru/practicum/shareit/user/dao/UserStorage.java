package ru.practicum.shareit.user.dao;

import ru.practicum.shareit.user.User;

import java.util.Collection;
import java.util.Optional;

public interface UserStorage {
    Collection<User> getAllUsers();

    User saveUser(User user);

    Optional<User> updateUser(Integer userId, User user);

    Optional<User> getUserById(Integer id);

    void checkEmailUniqueness(Integer userId, String email);

    void deleteUserById(Integer id);
}