package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.UserCreationDTO;
import ru.practicum.shareit.user.dto.UserUpdateDto;

import javax.validation.Valid;


@RestController
@RequestMapping(path = "/users")
@RequiredArgsConstructor
@Slf4j
public class UserController {
    private final UserClient userClient;

    @GetMapping
    public ResponseEntity<Object> getAllUsers() {
        log.info("GET request to fetch collection of users received.");
        return userClient.getAllUsers();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getUser(@PathVariable("id") Integer id) {
        log.info("GET request to fetch user_id={} received.", id);
        return userClient.getUser(id);
    }


    @PostMapping
    public ResponseEntity<Object> createUser(@Valid @RequestBody UserCreationDTO userCreationDTO) {
        log.info("POST request to create {} received.", userCreationDTO);
        return userClient.createUser(userCreationDTO);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Object> updateUser(@PathVariable("id") Integer id, @Valid @RequestBody UserUpdateDto userUpdateDto) {
        log.info("PATCH request to update user_id={} received.", id);
        return userClient.updateUser(id, userUpdateDto);
    }

    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable("id") Integer id) {
        log.info("Delete request to remove user_id={} received.", id);
        userClient.deleteUserById(id);
    }

}
