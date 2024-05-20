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
        checkEmailUniq(user.getEmail());
        user.setId(idCounter);
        users.put(user.getId(), user);
        idCounter++;
        userMails.add(user.getEmail());
        return user;
    }

    @Override
    public Optional<User> update(Integer userId, User user) {
        if (users.containsKey(userId)) {
            user.setId(userId);

            String originalEmail = users.get(userId).getEmail();
            String newEmail = user.getEmail();
            if (!originalEmail.equals(newEmail)) {
                checkEmailUniq(newEmail);
                userMails.remove(originalEmail);
                userMails.add(newEmail);
            }

            users.put(userId, user);
            return Optional.of(user);
        }
        return Optional.empty();
    }

    @Override
    public Optional<User> findById(Integer id) {
        return Optional.ofNullable(users.get(id));
    }


    private void checkEmailUniq(String email) {
        if (userMails.contains(email)) {
            throw new NonUniqueEmail(String.format("Email = %s address already in use", email));
        }
    }

    @Override
    public void deleteById(Integer id) {
        String userMail = users.get(id).getEmail();
        userMails.remove(userMail);
        users.remove(id);
    }
}

