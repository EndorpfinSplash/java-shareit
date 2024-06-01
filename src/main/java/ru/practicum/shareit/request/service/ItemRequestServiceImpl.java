package ru.practicum.shareit.request.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.UserNotFoundException;
import ru.practicum.shareit.item.ItemMapper;
import ru.practicum.shareit.item.dao.ItemRepository;
import ru.practicum.shareit.item.dto.ItemForRequestorOutputDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.ItemRequest;
import ru.practicum.shareit.request.ItemRequestMapper;
import ru.practicum.shareit.request.dao.ItemRequestRepository;
import ru.practicum.shareit.request.dto.ItemRequestCreationDto;
import ru.practicum.shareit.request.dto.ItemRequestOutputDto;
import ru.practicum.shareit.request.dto.RequestWithItemsOutputDto;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.dao.UserRepository;

import java.text.MessageFormat;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ItemRequestServiceImpl implements ItemRequestService {

    private final ItemRequestRepository itemRequestRepository;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;

    public ItemRequestOutputDto createItemRequest(Integer requestorId, ItemRequestCreationDto itemRequestCreationDto) {
        User itemRequestor = userRepository.findById(requestorId).orElseThrow(() ->
                new UserNotFoundException(MessageFormat.format("User with id {0} not found", requestorId)));

        ItemRequest itemRequest = ItemRequestMapper.toItemRequest(itemRequestor, itemRequestCreationDto);

        ItemRequest itemRequestSaved = itemRequestRepository.save(itemRequest);
        return ItemRequestMapper.toItemRequestOutputDto(itemRequestSaved);
    }

    @Override
    public List<RequestWithItemsOutputDto> getAllUserItemRequestsWithListOfResponsedItems(Integer requestorId) {
        User requestor = userRepository.findById(requestorId).orElseThrow(() ->
                new UserNotFoundException(MessageFormat.format("User with id {0} not found", requestorId)));

        List<ItemRequest> allRequestsOfRequestor = itemRequestRepository.findAllByRequestorId(requestorId);

        List<RequestWithItemsOutputDto> requestsWithItemsOutputDto = allRequestsOfRequestor.stream()
                .map(itemRequest -> {
                    List<Item> itemsListByRequestId = itemRepository.findByRequestId(itemRequest.getId());
                    List<ItemForRequestorOutputDto> itemsForRequestOfRequestorOutputDto = itemsListByRequestId.stream()
                            .map(ItemMapper::toItemForRequestorOutputDto)
                            .collect(Collectors.toList());
                    return ItemRequestMapper.toRequestWithItemsOutputDto(itemRequest, itemsForRequestOfRequestorOutputDto);
                })
                .collect(Collectors.toList());
        return requestsWithItemsOutputDto;
    }

}
