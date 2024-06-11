package ru.practicum.shareit.request.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import ru.practicum.shareit.item.dao.ItemRepository;
import ru.practicum.shareit.request.ItemRequest;
import ru.practicum.shareit.request.dao.ItemRequestRepository;
import ru.practicum.shareit.request.dto.ItemRequestCreationDto;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.dao.UserRepository;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ItemRequestServiceImplTest {

    @Mock
    private ItemRequestRepository itemRequestRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private ItemRepository itemRepository;

    @InjectMocks
    private ItemRequestServiceImpl itemRequestService;


    private User ownerItem;
    private User booker;

    @BeforeEach
    void setUp() {
        booker = new User(1, "Test booker ", "booker@mail.com");
        ownerItem = new User(2, "Test Item Owner ", "Tester@mail.com");
    }

    @Test
    void createItemRequest() {
        when(itemRequestRepository.save(any(ItemRequest.class))).thenAnswer(invocationOnMock -> invocationOnMock.getArgument(0));
        when(userRepository.findById(booker.getId())).thenReturn(Optional.of(booker));

        ItemRequestCreationDto itemRequestCreationDto = ItemRequestCreationDto.builder()
                .description("Need test tool")
                .build();
        itemRequestService.createItemRequest(booker.getId(), itemRequestCreationDto);
        verify(itemRequestRepository, times(1)).save(any(ItemRequest.class));
    }

    @Test
    void getAllUserItemRequestsWithListOfResponsedItems() {
        when(userRepository.findById(anyInt())).thenReturn(Optional.of(booker));
        itemRequestService.getAllUserItemRequestsWithListOfResponsedItems(anyInt());
        verify(itemRequestRepository, times(1)).findAllByRequestorIdOrderByCreatedDesc(anyInt());
    }

    @Test
    void getAllItemRequests() {
        when(itemRequestRepository.findAllByRequestor_IdNot(anyInt(), any(Pageable.class))).thenReturn(Collections.emptyList());
        itemRequestService.getAllItemRequests(booker.getId(), 0, 10);
        verify(itemRequestRepository, times(1)).findAllByRequestor_IdNot(booker.getId(),
                PageRequest.of(0, 10, Sort.by(Sort.Direction.ASC, "created"))
        );
    }

    @Test
    void getItemRequestByIdWithResponses() {
        when(userRepository.findById(booker.getId())).thenReturn(Optional.ofNullable(booker));
        ItemRequest itemRequest = new ItemRequest(15, "Test item request", booker, LocalDateTime.now());
        when(itemRequestRepository.findById(itemRequest.getId())).thenReturn(Optional.of(itemRequest));
        itemRequestService.getItemRequestByIdWithResponses(booker.getId(), itemRequest.getId());
        verify(itemRequestRepository, times(1)).findById(15);
    }
}