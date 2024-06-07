package ru.practicum.shareit.booking;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import ru.practicum.shareit.booking.dto.BookingCreationDTO;
import ru.practicum.shareit.booking.dto.BookingOutputDto;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class BookingControllerTest {
    private final String paramBookingState = "ALL";
    private final int paramIdxFrom = 1;
    private final int paramPageSize = 10;
    private final Integer bookerUserId = 1;
    private final Integer bookingId = 1;

    @InjectMocks
    BookingController bookingController;

    @Mock
    private BookingService bookingService;

    private MockMvc mockMvc;

    private BookingOutputDto bookingOutputDto;

    private BookingCreationDTO bookingCreationDTO;

    private List<BookingOutputDto> bookingsOutputDtos;

    ObjectMapper mapper = JsonMapper.builder()
            .addModule(new JavaTimeModule())
            .build();


    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders
                .standaloneSetup(bookingController)
                .build();

        User testBookerUser = User.builder().id(bookerUserId).build();
        Item testItem = new Item();
        LocalDateTime startTime = LocalDateTime.now().plusHours(5);
        LocalDateTime endTime = LocalDateTime.now().plusDays(1);

        bookingCreationDTO = BookingCreationDTO.builder()
                .start(startTime)
                .end(endTime)
                .itemId(1)
                .build();

        bookingOutputDto = BookingOutputDto.builder()
                .id(bookingId)
                .start(startTime)
                .end(endTime)
                .item(testItem)
                .booker(testBookerUser)
                .status(BookingStatus.WAITING)
                .build();

        bookingsOutputDtos = List.of(bookingOutputDto);
    }

    @Test
    void createBooking() throws Exception {
        when(bookingService.createBooking(any(), anyInt()))
                .thenReturn(bookingOutputDto);

        mockMvc.perform(post("/bookings")
                        .content(mapper.writeValueAsString(bookingCreationDTO))
                        .header("X-Sharer-User-Id", bookerUserId)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(bookingOutputDto.getId()), Integer.class))
                .andExpect(jsonPath("$.item", is(bookingOutputDto.getItem()), Item.class))
                .andExpect(jsonPath("$.booker", is(bookingOutputDto.getBooker()), User.class));
        verify(bookingService, times(1)).createBooking(bookingCreationDTO, bookerUserId);
    }

    @Test
    void changeApproveBooking() throws Exception {
        when(bookingService.changeApproveBooking(anyInt(), anyInt(), anyBoolean()))
                .thenReturn(bookingOutputDto);

        mockMvc.perform(patch("/bookings/{bookingId}", bookingId)
                        .param("approved", "true")
                        .header("X-Sharer-User-Id", bookerUserId)
                        .characterEncoding(StandardCharsets.UTF_8)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(bookingOutputDto.getId()), Integer.class))
                .andExpect(jsonPath("$.item", is(bookingOutputDto.getItem()), Item.class))
                .andExpect(jsonPath("$.booker", is(bookingOutputDto.getBooker()), User.class));
        verify(bookingService, times(1)).changeApproveBooking(bookingId, bookerUserId, true);
    }

    @Test
    void getBooking() throws Exception {
        when(bookingService.getBooking(anyInt(), anyInt()))
                .thenReturn(bookingOutputDto);

        mockMvc.perform(get("/bookings/{bookingId}", bookingId)
                        .header("X-Sharer-User-Id", bookerUserId)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(bookingOutputDto.getId()), Integer.class))
                .andExpect(jsonPath("$.item", is(bookingOutputDto.getItem()), Item.class))
                .andExpect(jsonPath("$.booker", is(bookingOutputDto.getBooker()), User.class));
        verify(bookingService, times(1)).getBooking(bookerUserId, bookingId);

    }

    @Test
    void getAllBookerBookings() throws Exception {
        when(bookingService.findAllBookerBookingsWithState(anyInt(), anyString(), anyInt(), anyInt()))
                .thenReturn(bookingsOutputDtos);

        mockMvc.perform(get("/bookings")
                        .param("state", paramBookingState)
                        .param("from", String.valueOf(paramIdxFrom))
                        .param("size", String.valueOf(paramPageSize))
                        .header("X-Sharer-User-Id", bookerUserId)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk());
        verify(bookingService, times(1))
                .findAllBookerBookingsWithState(
                        bookerUserId,
                        paramBookingState,
                        paramIdxFrom,
                        paramPageSize
                );
    }

    @Test
    void getAllOwnerBookingRequestsInState() throws Exception {
        when(bookingService.findAllOwnerBookingsWithState(anyInt(), anyString(), anyInt(), anyInt()))
                .thenReturn(bookingsOutputDtos);

        mockMvc.perform(get("/bookings/owner")
                        .param("state", paramBookingState)
                        .param("from", String.valueOf(paramIdxFrom))
                        .param("size", String.valueOf(paramPageSize))
                        .header("X-Sharer-User-Id", bookerUserId)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk());
        verify(bookingService, times(1))
                .findAllOwnerBookingsWithState(
                        bookerUserId,
                        paramBookingState,
                        paramIdxFrom,
                        paramPageSize
                );
    }
}