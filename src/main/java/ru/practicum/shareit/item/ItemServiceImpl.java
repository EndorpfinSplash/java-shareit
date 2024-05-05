package ru.practicum.shareit.item;

import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

import java.util.Collection;
import java.util.List;

@Service
public class ItemServiceImpl implements ItemService {

    @Override
    public Item createItem(Integer userId, ItemDto itemDto) {
        return null;
    }

    @Override
    public Item updateItem(Integer ItemId, Integer userId, ItemDto itemDto) {
        return null;
    }

    @Override
    public Item getItemById(Integer itemId) {
        return null;
    }

    @Override
    public Collection<Item> getAllUserItems(Integer userId) {
        return null;
    }


    /**
     * Проверьте, что поиск возвращает только доступные для аренды вещи.
     * */
    @Override
    public Collection<Item> findItemByNameOrDescription(String text) {
        return List.of();
    }
}
