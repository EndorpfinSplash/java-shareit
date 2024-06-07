package ru.practicum.shareit.user.dao;


import org.springframework.stereotype.Repository;
import ru.practicum.shareit.exception.NonUniqueEmail;
import ru.practicum.shareit.user.User;

import java.util.*;


@Repository
public class InMemoryUserStorage implements UserStorage {
    private int idCounter = 1;
    private final Map<Integer, User> users = new HashMap<>();
    private final Set<String> userMails = new HashSet<>();


    @Override
    public Collection<User> findAll() {
        return users.values();
    }

    @Override
    public User save(User user) {
        checkEmailUniqueness(user);
        if (user.getId() == null) {
            user.setId(idCounter);
            idCounter++;
        }
        users.put(user.getId(), user);
        userMails.add(user.getEmail());
        return user;
    }


    @Override
    public Optional<User> findById(Integer id) {
        return Optional.ofNullable(users.get(id));
    }


    private void checkEmailUniqueness(User user) {
        if (user.getId() == null && userMails.contains(user.getEmail())) {
            throw new NonUniqueEmail(String.format("Email = %s address already in use", user.getEmail()));
        }
        if (user.getId() != null) {
            String originalEmail = users.get(user.getId()).getEmail();
            String newEmail = user.getEmail();
            if (!originalEmail.equalsIgnoreCase(newEmail) && userMails.contains(user.getEmail())) {
                throw new NonUniqueEmail(String.format("Email = %s address already in use", user.getEmail()));
            }
            userMails.remove(originalEmail);
            userMails.add(newEmail);
        }

    }

    @Override
    public void deleteById(Integer id) {
        String userMail = users.get(id).getEmail();
        userMails.remove(userMail);
        users.remove(id);
    }
}

