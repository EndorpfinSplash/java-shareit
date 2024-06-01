package ru.practicum.shareit.request;

import ru.practicum.shareit.item.dto.ItemForRequestorOutputDto;
import ru.practicum.shareit.request.dto.ItemRequestCreationDto;
import ru.practicum.shareit.request.dto.ItemRequestOutputDto;
import ru.practicum.shareit.request.dto.RequestWithItemsOutputDto;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserMapper;

import java.time.LocalDateTime;
import java.util.List;

public class ItemRequestMapper {
    public static ItemRequest toItemRequest(User requstor, ItemRequestCreationDto itemRequestCreationDto) {
        return ItemRequest.builder()
                .description(itemRequestCreationDto.getDescription())
                .requestor(requstor)
                .created(LocalDateTime.now())
                .build();
    }

    public static ItemRequestOutputDto toItemRequestOutputDto(ItemRequest itemRequest) {
        return ItemRequestOutputDto.builder()
                .id(itemRequest.getId())
                .description(itemRequest.getDescription())
                .requestor(UserMapper.toUserOutputDto(itemRequest.getRequestor()))
                .created(itemRequest.getCreated())
                .build();
    }

    public static RequestWithItemsOutputDto toRequestWithItemsOutputDto(ItemRequest itemRequest,
                                                                        List<ItemForRequestorOutputDto> allItemsForRequest) {
        return RequestWithItemsOutputDto.builder()
                .description(itemRequest.getDescription())
                .created(itemRequest.getCreated())
                .items(allItemsForRequest)
                .build();
    }
}
