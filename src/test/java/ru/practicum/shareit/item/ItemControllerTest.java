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
    private final Integer USER_ID = 10;
    private final Integer ITEM_ID = 1;
    public static final String TEST_ITEM_NAME = "Test Item";
    public static final String TEST_ITEM_DESCRIPTION = "Test description";
    public static final int PARAM_IDX_FROM = 1;
    public static final int PARAM_PAGE_SIZE = 10;
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
        itemOutputDto = ItemOutputDto.builder()
                .id(ITEM_ID)
                .name(TEST_ITEM_NAME)
                .description(TEST_ITEM_DESCRIPTION)
                .available(true)
                .build();
        itemCreationDto = ItemCreationDto.builder()
                .name(TEST_ITEM_NAME)
                .description(TEST_ITEM_DESCRIPTION)
                .available(true)
                .build();
        itemUserOutputDto = ItemUserOutputDto.builder()
                .id(ITEM_ID)
                .name(TEST_ITEM_NAME)
                .description(TEST_ITEM_DESCRIPTION)
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
                        .header("X-Sharer-User-Id", USER_ID)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(itemOutputDto.getId()), Integer.class))
                .andExpect(jsonPath("$.bookingStatus", is(itemOutputDto.getBookingStatus()), BookingStatus.class))
                .andExpect(jsonPath("$.owner", is(itemOutputDto.getOwner()), User.class));
        verify(itemService, times(1)).createItem(USER_ID, itemCreationDto);
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

        mockMvc.perform(patch("/items/{itemId}", ITEM_ID)
                        .content(mapper.writeValueAsString(itemUpdateDto))
                        .header("X-Sharer-User-Id", USER_ID)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(itemOutputDto.getId()), Integer.class))
                .andExpect(jsonPath("$.bookingStatus", is(itemOutputDto.getBookingStatus()), BookingStatus.class))
                .andExpect(jsonPath("$.owner", is(itemOutputDto.getOwner()), User.class));
        verify(itemService, times(1)).updateItem(ITEM_ID, USER_ID, itemUpdateDto);
    }

    @Test
    void getItemById() throws Exception {
        when(itemService.getItemById(anyInt(), anyInt()))
                .thenReturn(itemUserOutputDto);

        mockMvc.perform(get("/items/{itemId}", ITEM_ID)
                        .header("X-Sharer-User-Id", USER_ID)
                        .characterEncoding(StandardCharsets.UTF_8)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(itemUserOutputDto.getId()), Integer.class))
                .andExpect(jsonPath("$.comments", is(itemUserOutputDto.getComments()), List.class))
                .andExpect(jsonPath("$.lastBooking", is(itemUserOutputDto.getLastBooking()), ShortBookingView.class))
                .andExpect(jsonPath("$.nextBooking", is(itemUserOutputDto.getNextBooking()), ShortBookingView.class));
        verify(itemService, times(1)).getItemById(ITEM_ID, USER_ID);
    }

    @Test
    void getAllUserItems() throws Exception {
        when(itemService.getAllUserItems(anyInt(), anyInt(), anyInt()))
                .thenReturn(itemUserOutputDtoList);

        mockMvc.perform(get("/items")
                        .param("from", String.valueOf(PARAM_IDX_FROM))
                        .param("size", String.valueOf(PARAM_PAGE_SIZE))
                        .header("X-Sharer-User-Id", USER_ID)
                        .characterEncoding(StandardCharsets.UTF_8)
                )
                .andExpect(status().isOk());

        verify(itemService, times(1)).getAllUserItems(USER_ID, PARAM_IDX_FROM, PARAM_PAGE_SIZE);
    }

    @Test
    void findItemByNameOrDescription() throws Exception {
        final String searchRequest = "Searched Item";
        when(itemService.getItemByNameOrDescription(anyString(), anyInt(), anyInt()))
                .thenReturn(itemOutputDtoList);

        mockMvc.perform(get("/items/search")
                        .param("text", searchRequest)
                        .param("from", String.valueOf(PARAM_IDX_FROM))
                        .param("size", String.valueOf(PARAM_PAGE_SIZE))
                        .header("X-Sharer-User-Id", USER_ID)
                        .characterEncoding(StandardCharsets.UTF_8)
                )
                .andExpect(status().isOk());

        verify(itemService, times(1)).getItemByNameOrDescription(searchRequest, PARAM_IDX_FROM, PARAM_PAGE_SIZE);
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

        mockMvc.perform(post("/items/{itemId}/comment", ITEM_ID)
                        .content(mapper.writeValueAsString(commentCreationDto))
                        .header("X-Sharer-User-Id", USER_ID)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(itemService, times(1)).saveComment(USER_ID, ITEM_ID, commentCreationDto);
    }
}