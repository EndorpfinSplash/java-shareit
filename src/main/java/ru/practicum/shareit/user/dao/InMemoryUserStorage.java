package ru.practicum.shareit.user.dao;


import org.springframework.stereotype.Repository;
import ru.practicum.shareit.user.User;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;


@Repository
public class InMemoryUserStorage implements UserStorage {
    private int idCounter = 1;
    private final Map<Integer, User> users = new HashMap<>();


    @Override
    public Collection<User> getAllUsers() {
        return users.values();
    }

    @Override
    public User saveUser(User user) {
        user.setId(idCounter);
        users.put(user.getId(), user);
        idCounter++;
        return user;
    }

    @Override
    public Optional<User> updateUser(Integer userId, User user) {
        if (users.containsKey(userId)) {
            users.put(userId, user);
            return Optional.of(user);
        }
        return Optional.empty();
    }

    @Override
    public Optional<User> getUserById(Integer id) {
        return Optional.of(users.get(id));
    }

}

