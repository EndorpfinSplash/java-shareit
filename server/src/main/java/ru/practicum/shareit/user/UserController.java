package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.UserCreationDTO;
import ru.practicum.shareit.user.dto.UserOutputDto;
import ru.practicum.shareit.user.dto.UserUpdateDto;

import java.util.Collection;


@RestController
@RequestMapping(path = "/users")
@RequiredArgsConstructor
@Slf4j
public class UserController {
    private final UserService userService;

    @GetMapping
    public Collection<UserOutputDto> getAllUsers() {
        log.info("GET request to fetch collection of users received.");
        return userService.getAllUsers();
    }

    @GetMapping("/{id}")
    public UserOutputDto getUser(@PathVariable("id") Integer id) {
        log.info("GET request to fetch user_id={} received.", id);
        return userService.getUserById(id);
    }


    @PostMapping
    public UserOutputDto createUser(@RequestBody UserCreationDTO userCreationDTO) {
        log.info("POST request to create {} received.", userCreationDTO);
        UserOutputDto createdUser = userService.createUser(userCreationDTO);
        log.info("{} was created", userCreationDTO);
        return createdUser;
    }

    @PatchMapping("/{id}")
    public UserOutputDto updateUser(@PathVariable("id") Integer id,
                                    @RequestBody UserUpdateDto userUpdateDto) {
        log.info("PATCH request to update user_id={} received.", id);
        return userService.updateUser(id, userUpdateDto);
    }

    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable("id") Integer id) {
        log.info("Delete request to remove user_id={} received.", id);
        userService.deleteUserById(id);
    }

}
