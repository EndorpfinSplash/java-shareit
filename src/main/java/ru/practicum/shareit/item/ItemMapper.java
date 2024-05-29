package ru.practicum.shareit.item;

import ru.practicum.shareit.booking.dto.ShortBookingView;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.dto.ItemCreationDto;
import ru.practicum.shareit.item.dto.ItemOutputDto;
import ru.practicum.shareit.item.dto.ItemUpdateDto;
import ru.practicum.shareit.item.dto.ItemUserOutputDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;

public class ItemMapper {
    public static ItemOutputDto toItemOutDto(Item item) {
        return ItemOutputDto.builder()
                .id(item.getId())
                .name(item.getName())
                .description(item.getDescription())
                .available(item.getAvailable())
                .build();
    }

    public static Item toItem(ItemCreationDto itemCreationDto, User user) {
        if (itemCreationDto.getName().isBlank() || itemCreationDto.getDescription() == null || itemCreationDto.getAvailable() == null) {
            throw new ValidationException("Item body contains empty fields!");
        }

        return Item.builder()
                .name(itemCreationDto.getName())
                .description(itemCreationDto.getDescription())
                .available(itemCreationDto.getAvailable())
                .owner(user)
                .build();
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

        String itemNewDescription = itemUpdateDto.getDescription();
        if (itemNewDescription != null) {
            item.setDescription(itemNewDescription);
        }
        item.setOwner(user);

        return item;
    }

    public static ItemUserOutputDto toUserItemOutDto(Item item) {
        return ItemUserOutputDto.builder()
                .id(item.getId())
                .name(item.getName())
                .description(item.getDescription())
                .available(item.getAvailable())
                .build();
    }

    public static ItemUserOutputDto toUserItemOutDto(Item item, ShortBookingView lastBooking, ShortBookingView nextBooking) {
        ItemUserOutputDto userItemOutDto = toUserItemOutDto(item);
//        userItemOutDto.setBookingStartDate(booking.getStart());
//        userItemOutDto.setBookingEndDate(booking.getEnd());
        userItemOutDto.setLastBooking(lastBooking);
        userItemOutDto.setNextBooking(nextBooking);
        return userItemOutDto;
    }
}
