package ru.practicum.shareit.user.dao;

import ru.practicum.shareit.user.User;

import java.util.Collection;
import java.util.Optional;
import java.util.Set;

public interface UserStorage {
    Collection<User> getAllUsers();

    User saveUser(User user);

    Optional<User> updateUser(User user);

    Optional<User> getUserById(Integer id);

}