package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

import java.util.Collection;


@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
@Slf4j
public class ItemController {
    private final ItemService itemService;


    @PostMapping
    public Item createItem(@RequestBody ItemDto itemDto,
                           @RequestHeader("X-Sharer-User-Id") Integer userId
    ) {
        log.info("POST request to create {} item.", itemDto);
        Item createdItem = itemService.createItem(userId, itemDto);
        log.info("{} was created", createdItem);
        return createdItem;
    }


    @PatchMapping("/{itemId}")
    public Item updateItem(@RequestHeader("X-Sharer-User-Id") Integer userId,
                           @RequestBody ItemDto itemDto,
                           @PathVariable() Integer itemId) {
        log.info("Patch request to update {} item.", itemDto);
        Item updatedItem = itemService.updateItem(itemId, userId, itemDto);
        log.info("{} was updated", updatedItem);
        return updatedItem;
    }

    @GetMapping("/{itemId}")
    public Item getItemById(@PathVariable Integer itemId) {
        log.info("GET request to get item with id {}", itemId);
        return itemService.getItemById(itemId);
    }


    @GetMapping
    public Collection<Item> getAllUserItems(@RequestHeader("X-Sharer-User-Id") Integer userId) {
        log.info("GET request to get all items from user {}", userId);
        return itemService.getAllUserItems(userId);
    }

    @GetMapping("/search")
    public Collection<Item> findItemByNameOrDescription(@RequestParam(value = "text") String text) {
        log.info("GET request to search items by name or description id {}", text);
        return itemService.findItemByNameOrDescription(text);
    }

}
