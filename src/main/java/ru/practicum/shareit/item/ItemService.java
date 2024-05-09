package ru.practicum.shareit.item;

import ru.practicum.shareit.exception.ItemCouldntBeModified;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

import java.util.Collection;

public interface ItemService {
    Item createItem(Integer userId, ItemDto itemDto);

    Item updateItem(Integer itemId, Integer userId, ItemDto itemDto);

    Item getItemById(Integer itemId);

    Collection<Item> getAllUserItems(Integer userId);

    Collection<Item> findItemByNameOrDescription(String text);
}
