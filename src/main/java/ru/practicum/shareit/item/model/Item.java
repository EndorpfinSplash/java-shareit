package ru.practicum.shareit.item.model;

import lombok.*;
import ru.practicum.shareit.booking.BookingStatus;
import ru.practicum.shareit.request.ItemRequest;
import ru.practicum.shareit.user.User;

import javax.validation.constraints.NotNull;


@Data
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Item {
    private Integer id;
    @NotNull
    private String name;
    @NotNull
    private String description;
    @NotNull
    private Boolean available;
    private User owner;
    private ItemRequest request;

    private BookingStatus bookingStatus;

    public Boolean isAvailable() {
        return available;
    }
}
