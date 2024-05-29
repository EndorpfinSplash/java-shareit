package ru.practicum.shareit.item.dto;

import lombok.Builder;
import lombok.Data;
import ru.practicum.shareit.booking.BookingStatus;
import ru.practicum.shareit.booking.dto.ShortBookingView;
import ru.practicum.shareit.request.ItemRequest;
import ru.practicum.shareit.user.User;

import java.time.LocalDateTime;

@Data
@Builder
public class ItemUserOutputDto {
    private Integer id;
    private String name;
    private String description;
    private Boolean available;
    private User owner;
    private ItemRequest request;
    private BookingStatus bookingStatus;
    private LocalDateTime bookingStartDate;
    private LocalDateTime bookingEndDate;
    private ShortBookingView lastBooking;
    private ShortBookingView nextBooking;

    public Boolean isAvailable() {
        return available;
    }
}
