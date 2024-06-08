package ru.practicum.shareit.item;

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
import ru.practicum.shareit.booking.BookingStatus;
import ru.practicum.shareit.booking.dto.ShortBookingView;
import ru.practicum.shareit.comment.Dto.CommentCreationDto;
import ru.practicum.shareit.comment.Dto.CommentOutputDto;
import ru.practicum.shareit.item.dto.ItemCreationDto;
import ru.practicum.shareit.item.dto.ItemOutputDto;
import ru.practicum.shareit.item.dto.ItemUpdateDto;
import ru.practicum.shareit.item.dto.ItemUserOutputDto;
import ru.practicum.shareit.user.User;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class ItemControllerTest {
    private final Integer userId = 10;
    private final Integer itemId = 1;
    private final int paramIdxFrom = 0;
    private final int paramPageSize = 10;
    @InjectMocks
    private ItemController itemController;

    @Mock
    private ItemService itemService;

    private MockMvc mockMvc;

    private ItemOutputDto itemOutputDto;
    private ItemCreationDto itemCreationDto;
    private ItemUserOutputDto itemUserOutputDto;
    private List<ItemUserOutputDto> itemUserOutputDtoList;
    private Collection<ItemOutputDto> itemOutputDtoList;

    ObjectMapper mapper = new ObjectMapper();


    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(itemController).build();
        String testItemName = "Test Item";
        String testItemDescription = "Test description";
        itemOutputDto = ItemOutputDto.builder()
                .id(itemId)
                .name(testItemName)
                .description(testItemDescription)
                .available(true)
                .build();
        itemCreationDto = ItemCreationDto.builder()
                .name(testItemName)
                .description(testItemDescription)
                .available(true)
                .build();
        itemUserOutputDto = ItemUserOutputDto.builder()
                .id(itemId)
                .name(testItemName)
                .description(testItemDescription)
                .available(true)
                .build();

        itemUserOutputDtoList = List.of(itemUserOutputDto);
        itemOutputDtoList = List.of(itemOutputDto);
    }

    @Test
    void createItem() throws Exception {
        when(itemService.createItem(anyInt(), any(ItemCreationDto.class)))
                .thenReturn(itemOutputDto);

        mockMvc.perform(post("/items")
                        .content(mapper.writeValueAsString(itemCreationDto))
                        .header("X-Sharer-User-Id", userId)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(itemOutputDto.getId()), Integer.class))
                .andExpect(jsonPath("$.bookingStatus", is(itemOutputDto.getBookingStatus()), BookingStatus.class))
                .andExpect(jsonPath("$.owner", is(itemOutputDto.getOwner()), User.class));
        verify(itemService, times(1)).createItem(userId, itemCreationDto);
    }

    @Test
    void updateItem() throws Exception {
        ItemUpdateDto itemUpdateDto = ItemUpdateDto.builder()
                .name("Updated Item")
                .description("Updated description")
                .available(false)
                .build();
        when(itemService.updateItem(anyInt(), anyInt(), any(ItemUpdateDto.class)))
                .thenReturn(itemOutputDto);

        mockMvc.perform(patch("/items/{itemId}", itemId)
                        .content(mapper.writeValueAsString(itemUpdateDto))
                        .header("X-Sharer-User-Id", userId)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(itemOutputDto.getId()), Integer.class))
                .andExpect(jsonPath("$.bookingStatus", is(itemOutputDto.getBookingStatus()), BookingStatus.class))
                .andExpect(jsonPath("$.owner", is(itemOutputDto.getOwner()), User.class));
        verify(itemService, times(1)).updateItem(itemId, userId, itemUpdateDto);
    }

    @Test
    void getItemById() throws Exception {
        when(itemService.getItemById(anyInt(), anyInt()))
                .thenReturn(itemUserOutputDto);

        mockMvc.perform(get("/items/{itemId}", itemId)
                        .header("X-Sharer-User-Id", userId)
                        .characterEncoding(StandardCharsets.UTF_8)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(itemUserOutputDto.getId()), Integer.class))
                .andExpect(jsonPath("$.comments", is(itemUserOutputDto.getComments()), List.class))
                .andExpect(jsonPath("$.lastBooking", is(itemUserOutputDto.getLastBooking()), ShortBookingView.class))
                .andExpect(jsonPath("$.nextBooking", is(itemUserOutputDto.getNextBooking()), ShortBookingView.class));
        verify(itemService, times(1)).getItemById(itemId, userId);
    }

    @Test
    void getAllUserItems() throws Exception {
        when(itemService.getUserItems(anyInt(), anyInt(), anyInt()))
                .thenReturn(itemUserOutputDtoList);

        mockMvc.perform(get("/items")
                        .param("from", String.valueOf(paramIdxFrom))
                        .param("size", String.valueOf(paramPageSize))
                        .header("X-Sharer-User-Id", userId)
                        .characterEncoding(StandardCharsets.UTF_8)
                )
                .andExpect(status().isOk());

        verify(itemService, times(1)).getUserItems(userId, paramIdxFrom, paramPageSize);
    }

    @Test
    void findItemByNameOrDescription() throws Exception {
        final String searchRequest = "Searched Item";
        when(itemService.getItemByNameOrDescription(anyString(), anyInt(), anyInt()))
                .thenReturn(itemOutputDtoList);

        mockMvc.perform(get("/items/search")
                        .param("text", searchRequest)
                        .param("from", String.valueOf(paramIdxFrom))
                        .param("size", String.valueOf(paramPageSize))
                        .header("X-Sharer-User-Id", userId)
                        .characterEncoding(StandardCharsets.UTF_8)
                )
                .andExpect(status().isOk());

        verify(itemService, times(1)).getItemByNameOrDescription(searchRequest, paramIdxFrom, paramPageSize);
    }

    @Test
    void createComment() throws Exception {
        String testComment = "Test comment";
        String testCommentator = "Test commentator";
        CommentCreationDto commentCreationDto = CommentCreationDto.builder().text(testComment).build();
        CommentOutputDto commentOutputDto = CommentOutputDto.builder()
                .id(20)
                .created(LocalDateTime.now())
                .text(testComment)
                .authorName(testCommentator)
                .build();
        when(itemService.saveComment(anyInt(), anyInt(), any(CommentCreationDto.class)))
                .thenReturn(commentOutputDto);

        mockMvc.perform(post("/items/{itemId}/comment", itemId)
                        .content(mapper.writeValueAsString(commentCreationDto))
                        .header("X-Sharer-User-Id", userId)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(itemService, times(1)).saveComment(userId, itemId, commentCreationDto);
    }
}