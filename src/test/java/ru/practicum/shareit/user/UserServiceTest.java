package ru.practicum.shareit.user;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.user.dao.UserRepository;
import ru.practicum.shareit.user.dto.UserCreationDTO;
import ru.practicum.shareit.user.dto.UserUpdateDto;

import java.util.Collections;
import java.util.Optional;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    UserRepository userRepository;

    @InjectMocks
    UserService userService;

    private User user;


    @BeforeEach
    void setUp() {
        user = new User(1, "test user", "testuser@mail.com");
    }

    @Test
    void getAllUsers() {
        when(userRepository.findAll()).thenReturn(Collections.emptyList());
        userService.getAllUsers();
        verify(userRepository, times(1)).findAll();
    }

    @Test
    void createUser() {
        when(userRepository.save(any(User.class))).thenReturn(user);
        UserCreationDTO userCreationDTO = new UserCreationDTO("creation user", "creation@mail.com");
        userService.createUser(userCreationDTO);
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void updateUser() {
        UserUpdateDto userUpdateDto = new UserUpdateDto("new name", "new@email.com");
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenReturn(user);
        userService.updateUser(user.getId(), userUpdateDto);
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void getUserById() {
        when(userRepository.findById(anyInt())).thenReturn(Optional.of(user));
        userService.getUserById(anyInt());
        verify(userRepository, times(1)).findById(anyInt());
    }

    @Test
    void deleteUserById() {
        userService.deleteUserById(anyInt());
        verify(userRepository, times(1)).deleteById(anyInt());
    }
}