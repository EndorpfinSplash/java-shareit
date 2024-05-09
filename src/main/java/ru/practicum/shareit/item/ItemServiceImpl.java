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

    public Item updateItem(Integer itemId, Integer userId, ItemDto itemDto) {
        Item itemForUpdate = itemStorage.getItemById(itemId).orElseThrow(
                () -> new ItemNotFoundException(MessageFormat.format("Item with id {0} not found", itemId)));
        userStorage.getUserById(userId).orElseThrow(() ->
                new UserNotFoundException(MessageFormat.format("User with id {0} not found", userId)));
        if (!Objects.equals(itemForUpdate.getOwner().getId(), userId)) {
            throw new ItemCouldntBeModified(MessageFormat.format("User with id {0} can't modify foreign item", userId));
        }

        Boolean availableNewValue = itemDto.getAvailable();
        if (availableNewValue != null) {
            itemForUpdate.setAvailable(availableNewValue);
        }

        String itemNewName = itemDto.getName();
        if (itemNewName != null) {
            itemForUpdate.setName(itemNewName);
        }

        String itemNewDescription = itemDto.getDescription();
        if (itemNewDescription != null) {
            itemForUpdate.setDescription(itemNewDescription);
        }

        return itemStorage.updateItem(itemId, itemForUpdate).orElseThrow(() ->
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
