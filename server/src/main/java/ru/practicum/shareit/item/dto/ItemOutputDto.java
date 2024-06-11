package ru.practicum.shareit.item.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.booking.BookingStatus;
import ru.practicum.shareit.user.User;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ItemOutputDto {
    private Integer id;
    private String name;
    private String description;
    private Boolean available;
    private User owner;
    private Integer requestId;
    private BookingStatus bookingStatus;

}
