package ru.practicum.shareit.request;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import ru.practicum.shareit.request.dto.ItemRequestCreationDto;
import ru.practicum.shareit.request.dto.ItemRequestOutputDto;
import ru.practicum.shareit.request.dto.RequestWithItemsOutputDto;
import ru.practicum.shareit.request.service.ItemRequestService;
import ru.practicum.shareit.user.dto.UserOutputDto;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.Collections;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class ItemRequestControllerTest {
    private static final Integer USER_ID = 1;
    private static final Integer ITEM_REQUEST_ID = 2;
    public static final String TEST_TOOL_DESCRIPTION = "Need Testing tool description";
    @InjectMocks
    private ItemRequestController itemRequestController;

    @Mock
    private ItemRequestService itemRequestService;

    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper();

    private final ItemRequestOutputDto itemRequestOutputDto = ItemRequestOutputDto.builder()
            .id(ITEM_REQUEST_ID)
            .requestor(new UserOutputDto(3, "Test Reuquestor Name", "Test@Reuquestor.Email"))
            .description("Test ItemReuquest Description")
            .build();


    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(itemRequestController).build();
    }

    @Test
    void createItemRequest() throws Exception {

        ItemRequestCreationDto itemRequestCreationDto = ItemRequestCreationDto.builder()
                .description(TEST_TOOL_DESCRIPTION)
                .build();
        when(itemRequestService.createItemRequest(anyInt(), any(ItemRequestCreationDto.class))).thenReturn(itemRequestOutputDto);

        mockMvc.perform(post("/requests")
                        .content(objectMapper.writeValueAsString(itemRequestCreationDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .header("X-Sharer-User-Id", USER_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk());
        verify(itemRequestService, times(1)).createItemRequest(USER_ID, itemRequestCreationDto);
    }

    @Test
    void getAllUserItemRequestsWithListOfResponsedItems() throws Exception {
        when(itemRequestService.getAllUserItemRequestsWithListOfResponsedItems(anyInt())).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/requests")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .header("X-Sharer-User-Id", USER_ID)
                        .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk());
        verify(itemRequestService, times(1)).getAllUserItemRequestsWithListOfResponsedItems(USER_ID);
    }

    @Test
    void getAllItemRequests() throws Exception {
        Integer paramFromIdx = 1;
        Integer paramSize = 10;
        when(itemRequestService.getAllItemRequests(anyInt(), anyInt(), anyInt())).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/requests/all")
                        .param("from", String.valueOf(paramFromIdx))
                        .param("size", String.valueOf(paramSize))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .header("X-Sharer-User-Id", USER_ID)
                        .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk());
        verify(itemRequestService, times(1)).getAllItemRequests(USER_ID, paramFromIdx, paramSize);
    }

    @Test
    void getItemRequestWithListOfResponsedItems() throws Exception {
        RequestWithItemsOutputDto requestWithItemsOutputDto = RequestWithItemsOutputDto.builder()
                .id(ITEM_REQUEST_ID)
                .description(TEST_TOOL_DESCRIPTION)
                .created(LocalDateTime.now())
                .build();

        when(itemRequestService.getItemRequestByIdWithResponses(anyInt(), anyInt())).thenReturn(requestWithItemsOutputDto);

        mockMvc.perform(get("/requests/{id}", ITEM_REQUEST_ID)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .header("X-Sharer-User-Id", USER_ID)
                        .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk());
        verify(itemRequestService, times(1)).getItemRequestByIdWithResponses(USER_ID, ITEM_REQUEST_ID);
    }
}