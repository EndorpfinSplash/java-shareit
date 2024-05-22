package ru.practicum.shareit.item.dao;

import org.springframework.stereotype.Repository;
import ru.practicum.shareit.item.model.Item;

import java.util.*;
import java.util.stream.Collectors;

@Repository
public class ItemStorageImMemoryImpl implements ItemStorage {

    private int idCounter = 1;
    private final Map<Integer, Item> items = new HashMap<>();
    private final Map<Integer, List<Item>> userItems = new HashMap<>();

    @Override
    public Item save(Item item) {
        if (item.getId() == null) {
            item.setId(idCounter);
            idCounter++;
        }
        items.put(item.getId(), item);
        userItems.computeIfAbsent(item.getOwner().getId(), k -> new LinkedList<>()).add(item);
        return item;
    }

    @Override
    public Optional<Item> updateItem(Integer itemId, Item item) {

        if (items.containsKey(itemId)) {
            Item oldItem = items.get(itemId);
            items.put(itemId, item);

            userItems.get(oldItem.getOwner().getId()).remove(oldItem);
            userItems.get(item.getOwner().getId()).add(item);

            return Optional.of(item);
        }
        return Optional.empty();
    }

    @Override
    public Optional<Item> findById(Integer itemId) {
        return Optional.ofNullable(items.get(itemId));
    }

    @Override
    public Collection<Item> findByOwner(Integer userId) {
        return userItems.get(userId);
    }


    @Override
    public Collection<Item> findByNameOrDescription(String text) {
        return items.values().stream()
                .filter(Item::isAvailable)
                .filter(item ->
                        item.getName().toLowerCase().contains(text.toLowerCase()) ||
                                (item.getDescription() != null && item.getDescription().toLowerCase().contains(text.toLowerCase()))
                )
                .collect(Collectors.toList());
    }
}
