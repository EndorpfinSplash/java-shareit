package ru.practicum.shareit.request.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.ItemRequestNotFoundException;
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
        userRepository.findById(requestorId).orElseThrow(() ->
                new UserNotFoundException(MessageFormat.format("User with id={0} not found", requestorId)));

        List<ItemRequest> allRequestsOfRequestor = itemRequestRepository.findAllByRequestorIdOrderByCreatedDesc(requestorId);

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

    @Override
    public List<ItemRequestOutputDto> getAllItemRequests(Integer userId, Integer from, Integer size) {
        Sort sortByCreatedDesc = Sort.by(Sort.Direction.ASC, "created");
        Pageable page = PageRequest.of(from > 0 ? from / size : 0, size, sortByCreatedDesc);
        Page<ItemRequest> allItemRequestOrderByCreatedDesc = itemRequestRepository.findAllByRequestor_IdNot(userId, page);
        return allItemRequestOrderByCreatedDesc.stream()
                .map(ItemRequestMapper::toItemRequestOutputDto)
                .collect(Collectors.toList());
    }

    @Override
    public RequestWithItemsOutputDto getItemRequestByIdWithResponses(Integer requestorId, Integer itemRequestId) {
        userRepository.findById(requestorId).orElseThrow(() ->
                new UserNotFoundException(MessageFormat.format("User with id {0} not found", requestorId)));
        ItemRequest itemRequest = itemRequestRepository.findById(itemRequestId).orElseThrow(() ->
                new ItemRequestNotFoundException(MessageFormat.format("ItemRequest with id= {0} not found", itemRequestId)));
        List<Item> itemsByRequestId = itemRepository.findByRequestId(itemRequestId);
        List<ItemForRequestorOutputDto> itemsForRequestorOutputDto = itemsByRequestId.stream()
                .map(ItemMapper::toItemForRequestorOutputDto)
                .collect(Collectors.toList());
        return ItemRequestMapper.toRequestWithItemsOutputDto(itemRequest, itemsForRequestorOutputDto);
    }

}
