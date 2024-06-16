package ru.practicum.shareit.request.dto;

import lombok.Builder;
import lombok.Data;
import ru.practicum.shareit.user.dto.UserOutputDto;

import java.time.LocalDateTime;

@Data
@Builder
public class ItemRequestOutputDto {

    private Integer id;
    private String description;
    private UserOutputDto requestor;
    private LocalDateTime created;
}
