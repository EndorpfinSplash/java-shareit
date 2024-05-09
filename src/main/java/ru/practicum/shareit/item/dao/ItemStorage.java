package ru.practicum.shareit.item.dao;

import ru.practicum.shareit.item.model.Item;

import java.util.Collection;
import java.util.Optional;

public interface ItemStorage {
    Item saveItem(Item item);

    Optional<Item> updateItem(Integer itemId, Item item);

    Optional<Item> getItemById(Integer itemId);

    Collection<Item> getAllUserItems(Integer userId);

    Collection<Item> findItemByNameOrDescription(String text);
}
