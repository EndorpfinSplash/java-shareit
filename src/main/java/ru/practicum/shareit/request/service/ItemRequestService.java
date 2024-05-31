package ru.practicum.shareit.request.service;

import ru.practicum.shareit.request.dto.ItemRequestCreationDto;
import ru.practicum.shareit.request.dto.ItemRequestOutputDto;

public interface ItemRequestService {
    ItemRequestOutputDto createItemRequest(Integer requestor, ItemRequestCreationDto itemRequestCreationDto);
}
