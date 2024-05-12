package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.ItemCouldntBeModified;
import ru.practicum.shareit.exception.ItemNotFoundException;
import ru.practicum.shareit.exception.UserNotFoundException;
import ru.practicum.shareit.item.dao.ItemStorage;
import ru.practicum.shareit.item.dto.ItemCreationDto;
import ru.practicum.shareit.item.dto.ItemOutputDto;
import ru.practicum.shareit.item.dto.ItemUpdateDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.dao.UserStorage;

import java.text.MessageFormat;
import java.util.Collection;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {

    private final ItemStorage itemStorage;
    private final UserStorage userStorage;

    public ItemOutputDto createItem(Integer userId, ItemCreationDto itemCreationDto) {
        User user = userStorage.findUserById(userId).orElseThrow(() ->
                new UserNotFoundException(MessageFormat.format("User with id {0} not found", userId)));
        Item item = ItemMapper.toItem(itemCreationDto, user);
        Item savedItem = itemStorage.saveItem(item);
        return ItemMapper.toItemDto(savedItem);
    }

    public ItemOutputDto updateItem(Integer itemId, Integer userId, ItemUpdateDto itemUpdateDto) {
        Item itemForUpdate = itemStorage.findItemById(itemId).orElseThrow(
                () -> new ItemNotFoundException(MessageFormat.format("Item with id {0} not found", itemId)));
        User user = userStorage.findUserById(userId).orElseThrow(() ->
                new UserNotFoundException(MessageFormat.format("User with id {0} not found", userId)));
        if (!Objects.equals(itemForUpdate.getOwner(), user)) {
            throw new ItemCouldntBeModified(MessageFormat.format("User with id {0} can't modify foreign item", userId));
        }

        Item editetItem = ItemMapper.toItem(itemForUpdate, itemUpdateDto, user);

        Item updatedItem = itemStorage.updateItem(itemId, editetItem).orElseThrow(() ->
                new ItemNotFoundException(MessageFormat.format("Item with id {0} not found", itemId)));
        return ItemMapper.toItemDto(updatedItem);
    }

    public ItemOutputDto getItemById(Integer itemId) {
        return ItemMapper.toItemDto(itemStorage.findItemById(itemId).orElseThrow(
                        () ->
                                new ItemNotFoundException(MessageFormat.format("Item with id={0} not found", itemId))
                )
        );
    }

    public Collection<ItemOutputDto> getAllUserItems(Integer userId) {
        return itemStorage.getAllUserItems(userId)
                .stream()
                .map(ItemMapper::toItemDto)
                .collect(Collectors.toList());
    }

    public Collection<ItemOutputDto> getItemByNameOrDescription(String text) {
        return itemStorage.findItemByNameOrDescription(text).stream()
                .map(ItemMapper::toItemDto)
                .collect(Collectors.toList());
    }
}
