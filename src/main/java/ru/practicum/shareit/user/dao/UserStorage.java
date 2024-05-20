package ru.practicum.shareit.user.dao;

import ru.practicum.shareit.user.User;

import java.util.Collection;
import java.util.Optional;

public interface UserStorage {
    Collection<User> findAll();

    User save(User user);

    Optional<User> update(Integer userId, User user);

    Optional<User> findById(Integer id);

    void deleteById(Integer id);
}