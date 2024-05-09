package ru.practicum.shareit.item.dao;

import org.springframework.stereotype.Repository;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

import java.util.*;

@Repository
public class ItemStorageImMemoryImpl implements ItemStorage {

    private int idCounter = 1;
    private final Map<Integer, Item> items = new HashMap<>();

    @Override
    public Item createItem(Integer userId, ItemDto itemDto) {
        return null;
    }

    @Override
    public Optional<Item> updateItem(Integer itemId, Item item) {

        if (items.containsKey(itemId)) {
            items.put(itemId, item);
            return Optional.of(item);
        }
        return Optional.empty();
    }

    @Override
    public Optional<Item> getItemById(Integer itemId) {
        return Optional.of(items.get(itemId));
    }

    @Override
    public Collection<Item> getAllUserItems(Integer userId) {
        return items.values();
    }


    /**
     * Проверьте, что поиск возвращает только доступные для аренды вещи.
     */
    @Override
    public Collection<Item> findItemByNameOrDescription(String text) {
        return List.of();
    }
}
