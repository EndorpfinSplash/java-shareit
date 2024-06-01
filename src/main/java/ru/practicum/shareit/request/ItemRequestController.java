package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.ItemRequestCreationDto;
import ru.practicum.shareit.request.dto.ItemRequestOutputDto;
import ru.practicum.shareit.request.dto.RequestWithItemsOutputDto;
import ru.practicum.shareit.request.service.ItemRequestService;

import javax.validation.Valid;
import java.util.List;


@RestController
@RequestMapping(path = "/requests")
@Slf4j
@RequiredArgsConstructor
public class ItemRequestController {

    private final ItemRequestService itemRequestService;

    @PostMapping
    public ItemRequestOutputDto createItemRequest(@RequestHeader("X-Sharer-User-Id") Integer requestor,
                                                  @Valid @RequestBody ItemRequestCreationDto itemRequestCreationDto) {
        log.info("POST request to create request {} received.", itemRequestCreationDto);
        ItemRequestOutputDto itemRequestOutputDto = itemRequestService.createItemRequest(requestor, itemRequestCreationDto);
        log.info("{} was created", itemRequestOutputDto);
        return itemRequestOutputDto;
    }

    @GetMapping
    public List<RequestWithItemsOutputDto> getAllUserItemRequestsWithListOfResponsedItems(
            @RequestHeader("X-Sharer-User-Id") Integer requestorId) {
        log.info("GET request for requestor_id={} to get requests responses received.", requestorId);
        List<RequestWithItemsOutputDto> result = itemRequestService.getAllUserItemRequestsWithListOfResponsedItems(requestorId);
        log.info("GET request for requestor_id={} with his all requests responses received.", requestorId);
        return result;
    }
}
