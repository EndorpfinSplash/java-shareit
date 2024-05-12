package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.ItemCreationDto;
import ru.practicum.shareit.item.dto.ItemOutputDto;
import ru.practicum.shareit.item.dto.ItemUpdateDto;

import java.util.Collection;


@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
@Slf4j
public class ItemController {
    private final ItemService itemService;


    @PostMapping
    public ItemOutputDto createItem(@RequestBody ItemCreationDto itemCreationDto,
                                    @RequestHeader("X-Sharer-User-Id") Integer userId
    ) {
        log.info("POST request to create {} item.", itemCreationDto);
        ItemOutputDto createdItem = itemService.createItem(userId, itemCreationDto);
        log.info("{} was created", createdItem);
        return createdItem;
    }


    @PatchMapping("/{itemId}")
    public ItemOutputDto updateItem(@RequestHeader("X-Sharer-User-Id") Integer userId,
                                    @RequestBody ItemUpdateDto itemUpdateDto,
                                    @PathVariable() Integer itemId) {
        log.info("Patch request to update {} item.", itemUpdateDto);
        ItemOutputDto updatedItem = itemService.updateItem(itemId, userId, itemUpdateDto);
        log.info("{} was updated", updatedItem);
        return updatedItem;
    }

    @GetMapping("/{itemId}")
    public ItemOutputDto getItemById(@PathVariable Integer itemId) {
        log.info("GET request to get item with id {}", itemId);
        return itemService.getItemById(itemId);
    }


    @GetMapping
    public Collection<ItemOutputDto> getAllUserItems(@RequestHeader("X-Sharer-User-Id") Integer userId) {
        log.info("GET request to get all items from user {}", userId);
        return itemService.getAllUserItems(userId);
    }

    @GetMapping("/search")
    public Collection<ItemOutputDto> findItemByNameOrDescription(@RequestParam(value = "text") String text) {
        log.info("GET request to search items by name or description id {}", text);
        return itemService.getItemByNameOrDescription(text);
    }

}
