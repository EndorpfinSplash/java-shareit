package ru.practicum.shareit.item.dao;

import org.springframework.stereotype.Repository;
import ru.practicum.shareit.item.model.Item;

import java.util.*;
import java.util.stream.Collectors;

@Repository
public class ItemStorageImMemoryImpl implements ItemStorage {

    private int idCounter = 1;
    private final Map<Integer, Item> items = new HashMap<>();

    @Override
    public Item saveItem(Item item) {
        item.setId(idCounter);
        items.put(item.getId(), item);
        idCounter++;
        return item;
    }

    @Override
    public Optional<Item> updateItem(Integer itemId, Item item) {

        if (items.containsKey(itemId)) {
            item.setId(itemId);
            items.put(itemId, item);
            return Optional.of(item);
        }
        return Optional.empty();
    }

    @Override
    public Optional<Item> findItemById(Integer itemId) {
        return Optional.ofNullable(items.get(itemId));
    }

    @Override
    public Collection<Item> getAllUserItems(Integer userId) {
        return items.values().stream()
                .filter(item -> Objects.equals(item.getOwner().getId(), userId))
                .collect(Collectors.toList());
    }


    @Override
    public Collection<Item> findItemByNameOrDescription(String text) {
        if (text == null || text.isEmpty()) {
            return Collections.emptyList();
        }
        return items.values().stream()
                .filter(Item::isAvailable)
                .filter(item ->
                        item.getName().toLowerCase().contains(text.toLowerCase()) ||
                                (item.getDescription() != null && item.getDescription().toLowerCase().contains(text.toLowerCase()))
                )
                .collect(Collectors.toList());
    }
}
