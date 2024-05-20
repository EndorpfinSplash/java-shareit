package ru.practicum.shareit.user;

import ru.practicum.shareit.user.dto.UserCreationDTO;
import ru.practicum.shareit.user.dto.UserOutputDto;
import ru.practicum.shareit.user.dto.UserUpdateDto;

public class UserMapper {

    public static UserOutputDto toUserOutputDto(User user) {
        return new UserOutputDto(
                user.getId(),
                user.getName(),
                user.getEmail()
        );
    }

    public static User toUser(UserCreationDTO userCreationDTO) {
        User user = User.builder().build();
        user.setEmail(userCreationDTO.getEmail());
        user.setName(userCreationDTO.getName());
        return user;
    }

    public static User toUser(User userForUpdate, UserUpdateDto userUpdateDto) {
        User user = userForUpdate.toBuilder().build();
        String email = userUpdateDto.getEmail();
        user.setId(userForUpdate.getId());
        if (email != null) {
            user.setEmail(email);
        }
        String name = userUpdateDto.getName();
        if (name != null) {
            user.setName(name);
        }
        return user;
    }

}
