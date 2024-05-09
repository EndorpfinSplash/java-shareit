package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.ItemCouldntBeModified;
import ru.practicum.shareit.exception.ItemNotFoundException;
import ru.practicum.shareit.exception.UserNotFoundException;
import ru.practicum.shareit.item.dao.ItemStorage;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.dao.UserStorage;

import java.text.MessageFormat;
import java.util.Collection;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {

    private final ItemStorage itemStorage;
    private final UserStorage userStorage;

    public Item createItem(Integer userId, ItemDto itemDto) {
        User user = userStorage.getUserById(userId).orElseThrow(() ->
                new UserNotFoundException(MessageFormat.format("User with id {0} not found", userId)));
        Item item = ItemMapper.toItem(itemDto, user);
        return itemStorage.saveItem(item);
    }

    public Item updateItem(Integer itemId, Integer userId, ItemDto itemDto)  {
        Item notEditedItem = itemStorage.getItemById(itemId).orElseThrow(() -> new ItemNotFoundException(MessageFormat.format("Item with id {0} not found", itemId)));
        if(!Objects.equals(notEditedItem.getOwner().getId(), userId)){
            throw new ItemCouldntBeModified(MessageFormat.format("User with id {0} can't modify foreign item", userId));
        }
        User user = userStorage.getUserById(userId).orElseThrow(() ->
                new UserNotFoundException(MessageFormat.format("User with id {0} not found", userId)));
        Item item = ItemMapper.toItem(itemDto, user);

        return itemStorage.updateItem(itemId, item).orElseThrow(() ->
                new ItemNotFoundException(MessageFormat.format("Item with id {0} not found", itemId)));
    }

    public Item getItemById(Integer itemId) {
        return itemStorage.getItemById(itemId).orElseThrow(() ->
                        new ItemNotFoundException(MessageFormat.format("Item with id={0} not found", itemId))
                );
    }

    public Collection<Item> getAllUserItems(Integer userId) {
        return itemStorage.getAllUserItems(userId);
    }

    public Collection<Item> findItemByNameOrDescription(String text) {
        return itemStorage.findItemByNameOrDescription(text);
    }
}
