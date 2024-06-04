package ru.practicum.shareit.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import ru.practicum.shareit.user.dto.UserCreationDTO;
import ru.practicum.shareit.user.dto.UserOutputDto;
import ru.practicum.shareit.user.dto.UserUpdateDto;

import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {

    private static final Integer USER_ID = 1;

    @InjectMocks
    private UserController userController;

    @Mock
    private UserService userService;

    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper();

    private final UserOutputDto userOutputDto = UserOutputDto.builder()
            .id(USER_ID)
            .name("User Name")
            .email("TEST@EMAIL.com")
            .build();


    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
    }

    @Test
    void getAllUsers() throws Exception {
        when(userService.getAllUsers()).thenReturn(List.of(userOutputDto));

        mockMvc.perform(get("/users")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk());
        verify(userService, times(1)).getAllUsers();
    }

    @Test
    void getUser() throws Exception {
        when(userService.getUserById(anyInt())).thenReturn(userOutputDto);

        mockMvc.perform(get("/users/{id}", USER_ID)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(userOutputDto.getId()), Integer.class));
        verify(userService, times(1)).getUserById(USER_ID);
    }

    @Test
    void createUser() throws Exception {
        UserCreationDTO userCreationDTO = UserCreationDTO.builder()
                .name("TEST_USERNAME")
                .email("TEST@EMAIL.com")
                .build();

        when(userService.createUser(any(UserCreationDTO.class))).thenReturn(userOutputDto);

        mockMvc.perform(post("/users")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .content(objectMapper.writeValueAsString(userCreationDTO))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                ).andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(userOutputDto.getId()), Integer.class));
        verify(userService, times(1)).createUser(userCreationDTO);
    }

    @Test
    void updateUser() throws Exception {
        UserUpdateDto userUpdateDto = UserUpdateDto.builder()
                .name("Updated test name")
                .build();

        when(userService.updateUser(anyInt(), any(UserUpdateDto.class))).thenReturn(userOutputDto);

        mockMvc.perform(patch("/users/{id}", USER_ID)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .content(objectMapper.writeValueAsString(userUpdateDto))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                ).andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(userOutputDto.getId()), Integer.class));
        verify(userService, times(1)).updateUser(USER_ID, userUpdateDto);
    }

    @Test
    void deleteUser() throws Exception {
        mockMvc.perform(delete("/users/{id}", USER_ID)
                .characterEncoding(StandardCharsets.UTF_8)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk());
        verify(userService, times(1)).deleteUserById(USER_ID);
    }
}