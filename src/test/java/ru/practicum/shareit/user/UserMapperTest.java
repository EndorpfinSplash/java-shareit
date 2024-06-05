package ru.practicum.shareit.user;

import org.junit.jupiter.api.Test;
import ru.practicum.shareit.user.dto.UserCreationDTO;
import ru.practicum.shareit.user.dto.UserOutputDto;
import ru.practicum.shareit.user.dto.UserUpdateDto;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class UserMapperTest {
    final String TEST_USER_NAME = "Test User";
    final String TEST_USER_EMAIL = "TEST@EMAIL.com";

    @Test
    void toUserOutputDto() {

        User testUser = User.builder()
                .id(1)
                .name(TEST_USER_NAME)
                .email(TEST_USER_EMAIL)
                .build();
        UserOutputDto resultUserOutputDto = UserMapper.toUserOutputDto(testUser);
        assertNotNull(resultUserOutputDto);
        assertEquals(testUser.getId(), resultUserOutputDto.getId());
        assertEquals(testUser.getName(), resultUserOutputDto.getName());
        assertEquals(testUser.getEmail(), resultUserOutputDto.getEmail());

    }

    @Test
    void toUser() {
        UserCreationDTO originUserCreationDTO = UserCreationDTO.builder()
                .name(TEST_USER_NAME)
                .email(TEST_USER_EMAIL)
                .build();
        User resultUser = UserMapper.toUser(originUserCreationDTO);
        assertNotNull(resultUser);
        assertEquals(TEST_USER_NAME, resultUser.getName());
        assertEquals(TEST_USER_EMAIL, resultUser.getEmail());
    }

    @Test
    void testToUser() {
        User userForUpdate = User.builder()
                .id(7)
                .name("origin name")
                .email("origin@email.com")
                .build();
        UserUpdateDto userUpdateDto = UserUpdateDto.builder()
                .name(TEST_USER_NAME)
                .email(TEST_USER_EMAIL)
                .build();
        User resultUser = UserMapper.toUser(userForUpdate, userUpdateDto);

        assertNotNull(resultUser);
        assertEquals(TEST_USER_NAME, resultUser.getName());
        assertEquals(TEST_USER_EMAIL, resultUser.getEmail());
        assertEquals(userForUpdate.getId(), resultUser.getId());
    }
}