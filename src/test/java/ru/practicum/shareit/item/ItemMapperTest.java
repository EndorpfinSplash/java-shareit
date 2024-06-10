package ru.practicum.shareit.item;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.practicum.shareit.comment.dto.CommentOutputDto;
import ru.practicum.shareit.item.dto.*;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.ItemRequest;
import ru.practicum.shareit.user.User;

import java.util.List;

class ItemMapperTest {
    public static final int TEST_ITEM_REQUEST_ID = 4;
    final User originTestUser = User.builder()
            .id(2)
            .name("Test User")
            .build();
    final ItemRequest testItemRequest = ItemRequest.builder()
            .id(TEST_ITEM_REQUEST_ID)
            .build();
    final Item originTestItem = Item.builder()
            .id(3)
            .name("Test Item")
            .available(true)
            .request(testItemRequest)
            .build();

    @Test
    void toItemOutDto() {
        ItemOutputDto resultItemOutDto = ItemMapper.toItemOutDto(originTestItem);
        Assertions.assertNotNull(resultItemOutDto);
        Assertions.assertEquals(originTestItem.getId(), resultItemOutDto.getId());
        Assertions.assertEquals(originTestItem.getName(), resultItemOutDto.getName());
        Assertions.assertEquals(originTestItem.getDescription(), resultItemOutDto.getDescription());
        Assertions.assertEquals(originTestItem.getAvailable(), resultItemOutDto.getAvailable());
        Assertions.assertEquals(originTestItem.getRequest().getId(), resultItemOutDto.getRequestId());

    }

    @Test
    void toItem() {
        ItemCreationDto originItemCreationDto = ItemCreationDto.builder()
                .name("Test creation Item")
                .description("Test Item creation description")
                .available(true)
                .requestId(TEST_ITEM_REQUEST_ID)
                .build();

        Item resultItem = ItemMapper.toItem(originItemCreationDto, originTestUser, testItemRequest);
        Assertions.assertNotNull(resultItem);
        Assertions.assertEquals("Test creation Item", resultItem.getName());
        Assertions.assertEquals("Test Item creation description", resultItem.getDescription());
        Assertions.assertEquals(true, resultItem.getAvailable());
        Assertions.assertEquals(originTestItem.getRequest().getId(), resultItem.getRequest().getId());
    }

    @Test
    void testToItem() {
        Item itemForUpdate = Item.builder()
                .id(10)
                .name("Test Item for update")
                .description("Test Item description")
                .available(true)
                .request(testItemRequest)
                .build();
        String testItemUpdatedName = "Test Item updated";
        ItemUpdateDto originItemUpdateDto = ItemUpdateDto.builder()
                .name(testItemUpdatedName)
                .description("Test Item description also updated")
                .available(false)
                .build();
        Item resultItemUpdated = ItemMapper.toItem(itemForUpdate, originItemUpdateDto, originTestUser);
        Assertions.assertNotNull(resultItemUpdated);
        Assertions.assertEquals(itemForUpdate.getId(), resultItemUpdated.getId());
        Assertions.assertEquals(originItemUpdateDto.getName(), resultItemUpdated.getName());
        Assertions.assertEquals(originItemUpdateDto.getDescription(), resultItemUpdated.getDescription());
        Assertions.assertEquals(originItemUpdateDto.getAvailable(), resultItemUpdated.getAvailable());
    }

    @Test
    void toUserItemOutDto() {
        CommentOutputDto commentOutputDto = CommentOutputDto.builder()
                .text("test comment")
                .authorName("test author")
                .build();
        ItemUserOutputDto resultUserItemOutDto = ItemMapper.toUserItemOutDto(originTestItem, List.of(commentOutputDto));
        Assertions.assertNotNull(resultUserItemOutDto);
        Assertions.assertEquals(originTestItem.getId(), resultUserItemOutDto.getId());
        Assertions.assertEquals(originTestItem.getName(), resultUserItemOutDto.getName());
        Assertions.assertEquals(originTestItem.getDescription(), resultUserItemOutDto.getDescription());
        Assertions.assertEquals(originTestItem.getAvailable(), resultUserItemOutDto.getAvailable());
        Assertions.assertEquals(1, resultUserItemOutDto.getComments().size());

    }

    @Test
    void toItemForRequestorOutputDto() {
        ItemForRequestorOutputDto resultItemForRequestorOutputDto = ItemMapper.toItemForRequestorOutputDto(originTestItem);
        Assertions.assertNotNull(resultItemForRequestorOutputDto);
        Assertions.assertEquals(originTestItem.getId(), resultItemForRequestorOutputDto.getId());
        Assertions.assertEquals(originTestItem.getName(), resultItemForRequestorOutputDto.getName());
        Assertions.assertEquals(originTestItem.getDescription(), resultItemForRequestorOutputDto.getDescription());
        Assertions.assertEquals(originTestItem.getAvailable(), resultItemForRequestorOutputDto.getAvailable());
        Assertions.assertEquals(originTestItem.getRequest().getId(), resultItemForRequestorOutputDto.getRequestId());

    }
}