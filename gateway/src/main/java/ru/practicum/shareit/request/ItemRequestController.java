package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.ItemRequestCreationDto;

import javax.validation.Valid;
import javax.validation.constraints.PositiveOrZero;


@RestController
@RequestMapping(path = "/requests")
@Slf4j
@RequiredArgsConstructor
@Validated
public class ItemRequestController {

    private final ItemRequestClient itemRequestClient;

    @PostMapping
    public ResponseEntity<Object> createItemRequest(
            @RequestHeader("X-Sharer-User-Id") Integer requestor,
            @Valid @RequestBody ItemRequestCreationDto itemRequestCreationDto
    ) {
        log.info("POST request to create request {} received.", itemRequestCreationDto);
        return itemRequestClient.createItemRequest(requestor, itemRequestCreationDto);
    }

    @GetMapping
    public ResponseEntity<Object> getAllUserItemRequestsWithListOfResponsedItems(
            @RequestHeader("X-Sharer-User-Id") Integer requestorId
    ) {
        log.info("GET request for requestor_id={} to get requests responses received.", requestorId);
        return itemRequestClient.getAllUserItemRequestsWithListOfResponsedItems(requestorId);
    }

    @GetMapping("/all")
    public ResponseEntity<Object> getAllItemRequests(
            @RequestHeader("X-Sharer-User-Id") Integer userId,
            @RequestParam(value = "from", defaultValue = "0") @PositiveOrZero Integer from,
            @RequestParam(value = "size", defaultValue = "10") Integer size
    ) {
        log.info("GET all request from user_id={} received with params from={} and size={}.", userId, from, size);
        return itemRequestClient.getAllItemRequests(userId, from, size);
    }

    @GetMapping("{requestId}")
    public ResponseEntity<Object> getItemRequestWithListOfResponsedItems(
            @PathVariable(name = "requestId") Integer requestId,
            @RequestHeader("X-Sharer-User-Id") Integer requestorId) {
        log.info("GET request from requestor_id={} to get info about request_id={} .", requestorId, requestId);
        return itemRequestClient.getItemRequestByIdWithResponses(requestorId, requestId);
    }
}
