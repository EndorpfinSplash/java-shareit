package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.ItemNotFoundException;
import ru.practicum.shareit.exception.UserNotFoundException;
import ru.practicum.shareit.item.dao.ItemStorage;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.dao.UserStorage;

import java.text.MessageFormat;
import java.util.Collection;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {

    private final ItemStorage itemStorage;
    private final UserStorage userStorage;

    public Item createItem(Integer userId, ItemDto itemDto) {
        return itemStorage.createItem(userId, itemDto);
    }

    public Item updateItem(Integer itemId, Integer userId, ItemDto itemDto) {
         userStorage.getUserById(userId).orElseThrow(() ->
                 new UserNotFoundException(MessageFormat.format("User with id {0} not found", userId)));
         Item item = ItemMapper.toItemDto()

        return itemStorage.updateItem(itemId, itemDto).orElseThrow(() ->
                new ItemNotFoundException(MessageFormat.format("Item with id {0} not found", itemId)));
    }

    public Item getItemById(Integer itemId) {
        return itemStorage.getItemById(itemId).orElseThrow(() ->
//                        () -> new UserNotFoundException(String.format("Item with id=%s absent", user.getId()))
                        new UserNotFoundException(MessageFormat.format("Item with id={0} not found", itemId))
                );
    }

    public Collection<Item> getAllUserItems(Integer userId) {
        return itemStorage.getAllUserItems(userId);
    }

    public Collection<Item> findItemByNameOrDescription(String text) {
        return itemStorage.findItemByNameOrDescription(text);
    }
}
