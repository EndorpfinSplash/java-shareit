package ru.practicum.shareit.item;

import ru.practicum.shareit.item.dto.ItemCreationDto;
import ru.practicum.shareit.item.dto.ItemOutputDto;
import ru.practicum.shareit.item.dto.ItemUpdateDto;
import ru.practicum.shareit.item.dto.ItemUserOutputDto;

import java.util.Collection;

public interface ItemService {
    ItemOutputDto createItem(Integer userId, ItemCreationDto itemDto);

    ItemOutputDto updateItem(Integer itemId, Integer userId, ItemUpdateDto itemUpdateDto);

    ItemUserOutputDto getItemById(Integer itemId, Integer userId);

    Collection<ItemUserOutputDto> getAllUserItems(Integer userId);

    Collection<ItemOutputDto> getItemByNameOrDescription(String text);
}
