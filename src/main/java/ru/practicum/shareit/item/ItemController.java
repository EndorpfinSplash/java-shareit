package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.comment.Dto.CommentCreationDto;
import ru.practicum.shareit.comment.Dto.CommentOutputDto;
import ru.practicum.shareit.item.dto.ItemCreationDto;
import ru.practicum.shareit.item.dto.ItemOutputDto;
import ru.practicum.shareit.item.dto.ItemUpdateDto;
import ru.practicum.shareit.item.dto.ItemUserOutputDto;

import javax.validation.Valid;
import javax.validation.constraints.PositiveOrZero;
import java.util.Collection;
import java.util.Collections;


@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
@Slf4j
@Validated
public class ItemController {
    private final ItemService itemService;


    @PostMapping
    public ItemOutputDto createItem(@Valid @RequestBody ItemCreationDto itemCreationDto,
                                    @RequestHeader("X-Sharer-User-Id") Integer userId
    ) {
        log.info("POST request to create {} item.", itemCreationDto);
        ItemOutputDto createdItem = itemService.createItem(userId, itemCreationDto);
        log.info("{} was created", createdItem);
        return createdItem;
    }


    @PatchMapping("/{itemId}")
    public ItemOutputDto updateItem(@RequestHeader("X-Sharer-User-Id") Integer userId,
                                    @Valid @RequestBody ItemUpdateDto itemUpdateDto,
                                    @PathVariable() Integer itemId) {
        log.info("Patch request to update {} item.", itemUpdateDto);
        ItemOutputDto updatedItem = itemService.updateItem(itemId, userId, itemUpdateDto);
        log.info("{} was updated", updatedItem);
        return updatedItem;
    }

    @GetMapping("/{itemId}")
    public ItemUserOutputDto getItemById(@RequestHeader("X-Sharer-User-Id") Integer userId,
                                         @PathVariable Integer itemId) {
        log.info("GET request to get item with id {}", itemId);
        return itemService.getItemById(itemId, userId);
    }


    @GetMapping
    public Collection<ItemUserOutputDto> getAllUserItems(
            @RequestHeader("X-Sharer-User-Id") Integer userId,
            @RequestParam(value = "from", defaultValue = "0") @PositiveOrZero Integer from,
            @RequestParam(value = "size", defaultValue = "10") Integer size
    ) {
        log.info("GET request to get all items from user {}", userId);
        return itemService.getUserItems(userId, from, size);
    }

    @GetMapping("/search")
    public Collection<ItemOutputDto> findItemByNameOrDescription(
            @RequestParam(value = "text") String text,
            @RequestParam(value = "from", defaultValue = "0") @PositiveOrZero Integer from,
            @RequestParam(value = "size", defaultValue = "10") Integer size
    ) {
        if (text == null || text.isEmpty()) {
            return Collections.emptyList();
        }
        log.info("GET request to search items by name or description id {}", text);
        return itemService.getItemByNameOrDescription(text, from, size);
    }

    @PostMapping("/{itemId}/comment")
    public CommentOutputDto createComment(@RequestHeader("X-Sharer-User-Id") Integer commentatorId,
                                          @PathVariable Integer itemId,
                                          @Valid @RequestBody CommentCreationDto commentCreationDto
    ) {
        return itemService.saveComment(commentatorId, itemId, commentCreationDto);
    }

}