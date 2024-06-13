package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.CommentCreationDto;
import ru.practicum.shareit.item.dto.ItemCreationDto;
import ru.practicum.shareit.item.dto.ItemUpdateDto;

import javax.validation.Valid;
import javax.validation.constraints.PositiveOrZero;
import java.util.Collections;


@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
@Slf4j
@Validated
public class ItemController {
    private final ItemClient itemClient;


    @PostMapping
    public ResponseEntity<Object> createItem(@Valid @RequestBody ItemCreationDto itemCreationDto,
                                             @RequestHeader("X-Sharer-User-Id") Integer userId
    ) {
        log.info("POST request to create {} item.", itemCreationDto);
        return itemClient.createItem(userId, itemCreationDto);
    }


    @PatchMapping("/{itemId}")
    public ResponseEntity<Object> updateItem(@RequestHeader("X-Sharer-User-Id") Integer userId,
                                             @Valid @RequestBody ItemUpdateDto itemUpdateDto,
                                             @PathVariable() Integer itemId) {
        log.info("Patch request to update {} item.", itemUpdateDto);


        return itemClient.updateItem(itemId, userId, itemUpdateDto);
    }

    @GetMapping("/{itemId}")
    public ResponseEntity<Object> getItemById(@RequestHeader("X-Sharer-User-Id") Integer userId,
                                              @PathVariable Integer itemId) {
        log.info("GET request to get item with id {}", itemId);
        return itemClient.getItemById(itemId, userId);
    }


    @GetMapping
    public ResponseEntity<Object> getAllUserItems(
            @RequestHeader("X-Sharer-User-Id") Integer userId,
            @RequestParam(value = "from", defaultValue = "0") @PositiveOrZero Integer from,
            @RequestParam(value = "size", defaultValue = "10") Integer size
    ) {
        log.info("GET request to get all items from user {}", userId);
        return itemClient.getUserItems(userId, from, size);
    }

    @GetMapping("/search")
    public ResponseEntity<Object> findItemByNameOrDescription(
            @RequestParam(value = "text") String text,
            @RequestParam(value = "from", defaultValue = "0") @PositiveOrZero Integer from,
            @RequestParam(value = "size", defaultValue = "10") Integer size,
            @RequestHeader("X-Sharer-User-Id") Integer userId
    ) {
        if (text == null || text.isEmpty()) {
            return ResponseEntity.ok().body(Collections.emptyList());
        }
        log.info("GET request to search items by name or description id {}", text);
        return itemClient.getItemByNameOrDescription(text, from, size, userId);
    }

    @PostMapping("/{itemId}/comment")
    public ResponseEntity<Object> createComment(@RequestHeader("X-Sharer-User-Id") Integer commentatorId,
                                                @PathVariable Integer itemId,
                                                @Valid @RequestBody CommentCreationDto commentCreationDto
    ) {
        return itemClient.saveComment(commentatorId, itemId, commentCreationDto);
    }

}