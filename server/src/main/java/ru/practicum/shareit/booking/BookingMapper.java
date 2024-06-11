package ru.practicum.shareit.booking;

import ru.practicum.shareit.booking.dto.BookingCreationDTO;
import ru.practicum.shareit.booking.dto.BookingOutputDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;

public class BookingMapper {
    public static Booking toBooking(BookingCreationDTO bookingCreationDTO, User user, Item item) {
        return Booking.builder()
                .start(bookingCreationDTO.getStart())
                .end(bookingCreationDTO.getEnd())
                .item(item)
                .booker(user)
                .bookingStatus(BookingStatus.WAITING)
                .build();
    }

    public static BookingOutputDto toOutBookingDto(Booking booking) {
        return BookingOutputDto.builder()
                .id(booking.getId())
                .start(booking.getStart())
                .end(booking.getEnd())
                .item(booking.getItem())
                .booker(booking.getBooker())
                .status(booking.getBookingStatus())
                .build();
    }
}
