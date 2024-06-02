package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.ItemRequestCreationDto;
import ru.practicum.shareit.request.dto.ItemRequestOutputDto;
import ru.practicum.shareit.request.dto.RequestWithItemsOutputDto;
import ru.practicum.shareit.request.service.ItemRequestService;

import javax.validation.Valid;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;


@RestController
@RequestMapping(path = "/requests")
@Slf4j
@RequiredArgsConstructor
@Validated
public class ItemRequestController {

    private final ItemRequestService itemRequestService;

    @PostMapping
    public ItemRequestOutputDto createItemRequest(
            @RequestHeader("X-Sharer-User-Id") Integer requestor,
            @Valid @RequestBody ItemRequestCreationDto itemRequestCreationDto
    ) {
        log.info("POST request to create request {} received.", itemRequestCreationDto);
        ItemRequestOutputDto itemRequestOutputDto = itemRequestService.createItemRequest(requestor, itemRequestCreationDto);
        log.info("{} was created", itemRequestOutputDto);
        return itemRequestOutputDto;
    }

    @GetMapping
    public List<RequestWithItemsOutputDto> getAllUserItemRequestsWithListOfResponsedItems(
            @RequestHeader("X-Sharer-User-Id") Integer requestorId
    ) {
        log.info("GET request for requestor_id={} to get requests responses received.", requestorId);
        List<RequestWithItemsOutputDto> result = itemRequestService.getAllUserItemRequestsWithListOfResponsedItems(requestorId);
        log.info("GET all requests for requestor_id={} with his all requests responses received.", requestorId);
        return result;
    }

    @GetMapping("/all")
    public List<ItemRequestOutputDto> getAllItemRequests(
            @RequestHeader("X-Sharer-User-Id") Integer userId,
            @RequestParam(value = "from", defaultValue = "0") @PositiveOrZero Integer from,
            @RequestParam(value = "size", defaultValue = "10") Integer size
    ) {
        log.info("GET all request from user_id={} received with params from={} and size={}.", userId, from, size);
        List<ItemRequestOutputDto> result = itemRequestService.getAllItemRequests(from, size);
        log.info("GET all item requests for user_id={} created.", userId);
        return result;
    }

    @GetMapping("{requestId}")
    public RequestWithItemsOutputDto getItemRequestWithListOfResponsedItems(
            @PathVariable(name = "requestId") Integer requestId,
            @RequestHeader("X-Sharer-User-Id") Integer requestorId) {
        log.info("GET request from requestor_id={} to get info about request_id={} .", requestorId, requestId);
        RequestWithItemsOutputDto result = itemRequestService.getItemRequestByIdWithResponses(requestId);
        log.info("GET request for requestor_id={} with his all requests responses received.", requestorId);
        return result;
    }
}
