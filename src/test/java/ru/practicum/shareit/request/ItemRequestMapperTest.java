package ru.practicum.shareit.request;

import org.junit.jupiter.api.Test;
import ru.practicum.shareit.item.dto.ItemForRequestorOutputDto;
import ru.practicum.shareit.request.dto.ItemRequestCreationDto;
import ru.practicum.shareit.request.dto.ItemRequestOutputDto;
import ru.practicum.shareit.request.dto.RequestWithItemsOutputDto;
import ru.practicum.shareit.user.User;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class ItemRequestMapperTest {
    public static final int TEST_ITEM_REQUEST_ID = 3;
    final User originTestRequestor = User.builder()
            .id(2)
            .name("Test User Requestor")
            .build();
    final ItemRequest originTestItemRequest = ItemRequest.builder()
            .id(TEST_ITEM_REQUEST_ID)
            .requestor(originTestRequestor)
            .description("Test Item Request description")
            .created(LocalDateTime.now())
            .build();

    @Test
    void toItemRequest() {
        ItemRequestCreationDto itemRequestCreationDto = ItemRequestCreationDto.builder()
                .description("Test Item Request description")
                .build();
        ItemRequest resultItemRequest = ItemRequestMapper.toItemRequest(originTestRequestor, itemRequestCreationDto);
        assertNotNull(resultItemRequest);
        assertEquals("Test Item Request description", resultItemRequest.getDescription());
        assertEquals(originTestRequestor, resultItemRequest.getRequestor());
    }

    @Test
    void toItemRequestOutputDto() {
        ItemRequestOutputDto resultItemRequestOutputDto = ItemRequestMapper.toItemRequestOutputDto(originTestItemRequest);
        assertNotNull(resultItemRequestOutputDto);
        assertEquals("Test Item Request description", resultItemRequestOutputDto.getDescription());
        assertEquals(originTestRequestor.getId(), resultItemRequestOutputDto.getRequestor().getId());
        assertNotNull(resultItemRequestOutputDto.getCreated());
    }

    @Test
    void toRequestWithItemsOutputDto() {
        ItemForRequestorOutputDto itemForRequestorOutputDto = ItemForRequestorOutputDto.builder()
                .name("Test Item responsed Request name")
                .description("Test Item Request description")
                .requestId(TEST_ITEM_REQUEST_ID)
                .available(true)
                .build();
        RequestWithItemsOutputDto resultRequestWithItemsOutputDto = ItemRequestMapper.toRequestWithItemsOutputDto(
                originTestItemRequest,
                List.of(itemForRequestorOutputDto)
        );
        assertNotNull(resultRequestWithItemsOutputDto);
        assertEquals(originTestItemRequest.getId(), resultRequestWithItemsOutputDto.getId());
        assertEquals(originTestItemRequest.getDescription(), resultRequestWithItemsOutputDto.getDescription());
        assertEquals(originTestItemRequest.getCreated(), resultRequestWithItemsOutputDto.getCreated());
        assertEquals(1, resultRequestWithItemsOutputDto.getItems().size());
        assertEquals(itemForRequestorOutputDto, resultRequestWithItemsOutputDto.getItems().get(0));
    }
}