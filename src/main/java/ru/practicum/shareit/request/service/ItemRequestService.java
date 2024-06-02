package ru.practicum.shareit.request.service;

import ru.practicum.shareit.request.dto.ItemRequestCreationDto;
import ru.practicum.shareit.request.dto.ItemRequestOutputDto;
import ru.practicum.shareit.request.dto.RequestWithItemsOutputDto;

import java.util.List;

public interface ItemRequestService {
    ItemRequestOutputDto createItemRequest(Integer requestor, ItemRequestCreationDto itemRequestCreationDto);

    List<RequestWithItemsOutputDto> getAllUserItemRequestsWithListOfResponsedItems(Integer requestorId);

    List<ItemRequestOutputDto> getAllItemRequests(Integer from, Integer size);

    RequestWithItemsOutputDto getItemRequestByIdWithResponses(Integer itemRequestId);
}
