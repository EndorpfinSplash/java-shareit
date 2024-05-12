package ru.practicum.shareit.user.dao;


import org.springframework.stereotype.Repository;
import ru.practicum.shareit.exception.NonUniqueEmail;
import ru.practicum.shareit.user.User;

import java.util.*;


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
        checkEmailUniqueness(user.getId(), user.getEmail());
        user.setId(idCounter);
        users.put(user.getId(), user);
        idCounter++;
        return user;
    }

    @Override
    public Optional<User> updateUser(Integer userId, User user) {
        if (users.containsKey(userId)) {
            user.setId(userId);
            users.put(userId, user);
            return Optional.of(user);
        }
        return Optional.empty();
    }

    @Override
    public Optional<User> findUserById(Integer id) {
        return Optional.ofNullable(users.get(id));
    }


    public void checkEmailUniqueness(Integer userId, String email) {
        if (users.values().stream()
                .filter(user -> !Objects.equals(user.getId(), userId))
                .map(User::getEmail)
                .filter(email::equals)
                .findFirst().orElse(null) != null) {
            throw new NonUniqueEmail(String.format("Email = %s address already in use", email));
        }
    }

    @Override
    public void deleteUserById(Integer id) {
        users.remove(id);
    }
}

