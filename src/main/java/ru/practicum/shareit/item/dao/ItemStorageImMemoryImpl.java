package ru.practicum.shareit.item.dao;

import org.springframework.stereotype.Repository;
import ru.practicum.shareit.item.model.Item;

import java.util.*;
import java.util.stream.Collectors;

@Repository
public class ItemStorageImMemoryImpl implements ItemStorage {

    private int idCounter = 1;
    private final Map<Integer, Item> items = new HashMap<>();
    private final Map<Integer, HashMap<Integer, Item>> userItems = new HashMap<>();

    @Override
    public Item saveItem(Item item) {
        item.setId(idCounter);
        items.put(item.getId(), item);
        idCounter++;
        userItems.computeIfAbsent(item.getOwner().getId(), k -> new HashMap<>()).put(item.getId(), item);
        return item;
    }

    @Override
    public Optional<Item> updateItem(Integer itemId, Item item) {

        if (items.containsKey(itemId)) {
            Item oldItem = items.get(itemId);
            items.put(itemId, item);

            userItems.get(oldItem.getOwner().getId()).remove(oldItem.getId());
            userItems.get(item.getOwner().getId()).put(item.getId(), item);

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
        return userItems.get(userId).values();
    }


    @Override
    public Collection<Item> findItemByNameOrDescription(String text) {
        return items.values().stream()
                .filter(Item::isAvailable)
                .filter(item ->
                        item.getName().toLowerCase().contains(text.toLowerCase()) ||
                                (item.getDescription() != null && item.getDescription().toLowerCase().contains(text.toLowerCase()))
                )
                .collect(Collectors.toList());
    }
}
