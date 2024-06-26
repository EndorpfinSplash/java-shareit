package ru.practicum.shareit.item;

import ru.practicum.shareit.comment.dto.CommentOutputDto;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.dto.*;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.ItemRequest;
import ru.practicum.shareit.user.User;

import java.util.List;

public class ItemMapper {
    public static ItemOutputDto toItemOutDto(Item item) {
        ItemOutputDto itemOutputDto = ItemOutputDto.builder()
                .id(item.getId())
                .name(item.getName())
                .description(item.getDescription())
                .available(item.getAvailable())
                .build();

        ItemRequest itemRequest = item.getRequest();
        if (itemRequest != null) {
            itemOutputDto.setRequestId(itemRequest.getId());
        }
        return itemOutputDto;
    }

    public static Item toItem(ItemCreationDto itemCreationDto, User user, ItemRequest itemRequest) {
        if (itemCreationDto.getName().isBlank() || itemCreationDto.getDescription() == null || itemCreationDto.getAvailable() == null) {
            throw new ValidationException("Item body contains empty fields!");
        }
        Item item = Item.builder()
                .name(itemCreationDto.getName())
                .description(itemCreationDto.getDescription())
                .available(itemCreationDto.getAvailable())
                .owner(user)
                .build();

        if (itemRequest != null) {
            item.setRequest(itemRequest);
        }
        return item;
    }

    public static Item toItem(Item itemForUpdate, ItemUpdateDto itemUpdateDto, User user) {
        Item item = itemForUpdate.toBuilder().build();
        Boolean availableNewValue = itemUpdateDto.getAvailable();
        if (availableNewValue != null) {
            item.setAvailable(availableNewValue);
        }

        String itemNewName = itemUpdateDto.getName();
        if (itemNewName != null) {
            item.setName(itemNewName);
        }

        Integer requestId = itemUpdateDto.getRequestId();
        if (requestId != null) {
            item.setId(requestId);
        }

        String itemNewDescription = itemUpdateDto.getDescription();
        if (itemNewDescription != null) {
            item.setDescription(itemNewDescription);
        }
        item.setOwner(user);

        return item;
    }

    public static ItemUserOutputDto toUserItemOutDto(Item item, List<CommentOutputDto> comments) {
        return ItemUserOutputDto.builder()
                .id(item.getId())
                .name(item.getName())
                .description(item.getDescription())
                .available(item.getAvailable())
                .comments(comments)
                .build();
    }

    public static ItemForRequestorOutputDto toItemForRequestorOutputDto(Item item) {
        return ItemForRequestorOutputDto.builder()
                .id(item.getId())
                .name(item.getName())
                .description(item.getDescription())
                .available(item.getAvailable())
                .requestId(item.getRequest().getId())
                .build();
    }

}
