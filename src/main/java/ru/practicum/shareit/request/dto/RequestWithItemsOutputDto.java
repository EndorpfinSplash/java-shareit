package ru.practicum.shareit.request.dto;

import lombok.Builder;
import lombok.Data;
import ru.practicum.shareit.item.dto.ItemForRequestorOutputDto;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class RequestWithItemsOutputDto {

    private Integer id;
    private String description;
    private LocalDateTime created;
    private List<ItemForRequestorOutputDto> items;
}
