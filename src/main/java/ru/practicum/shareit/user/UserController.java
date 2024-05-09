package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Collection;


@RestController
@RequestMapping(path = "/users")
@RequiredArgsConstructor
@Slf4j
public class UserController {
    private final UserService userService;

    @GetMapping
    public Collection<User> getAllUsers() {
        log.info("GET request to fetch collection of users received.");
        return userService.getAllUsers();
    }

    @GetMapping("/{id}")
    public User getUser(@PathVariable("id") Integer id) {
        log.info("GET request to fetch user_id={} received.", id);
        return userService.getUserById(id);
    }


    @PostMapping
    public User createUser(@Valid @RequestBody User user) {
        log.info("POST request to create {} received.", user);
        User createdUser = userService.createUser(user);
        log.info("{} was created", user);
        return createdUser;
    }

    @PatchMapping("/{id}")
    public User updateUser(@PathVariable("id") Integer id, @Valid @RequestBody User user) {
        log.info("PATCH request to update user_id={} received.", id);
        return userService.updateUser(id, user);
    }
}
