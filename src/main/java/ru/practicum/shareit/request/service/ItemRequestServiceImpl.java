package ru.practicum.shareit.request.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.UserNotFoundException;
import ru.practicum.shareit.request.ItemRequest;
import ru.practicum.shareit.request.ItemRequestMapper;
import ru.practicum.shareit.request.dao.ItemRequestRepository;
import ru.practicum.shareit.request.dto.ItemRequestCreationDto;
import ru.practicum.shareit.request.dto.ItemRequestOutputDto;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.dao.UserRepository;

import java.text.MessageFormat;

@Service
@RequiredArgsConstructor
public class ItemRequestServiceImpl implements ItemRequestService {

    private final ItemRequestRepository itemRequestRepository;
    private final UserRepository userRepository;

    public ItemRequestOutputDto createItemRequest(Integer requestorId, ItemRequestCreationDto itemRequestCreationDto) {
        User itemRequestor = userRepository.findById(requestorId).orElseThrow(() ->
                new UserNotFoundException(MessageFormat.format("User with id {0} not found", requestorId)));

        ItemRequest itemRequest = ItemRequestMapper.toItemRequest(itemRequestor, itemRequestCreationDto);

        ItemRequest itemRequestSaved = itemRequestRepository.save(itemRequest);
        return ItemRequestMapper.toItemRequestOutputDto(itemRequestSaved);
    }

}
