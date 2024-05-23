package ru.practicum.shareit.booking;

import ru.practicum.shareit.booking.dto.BookingCreationDTO;
import ru.practicum.shareit.booking.dto.BookingOutputDto;


public interface BookingService {
    BookingOutputDto createBooking(BookingCreationDTO bookingCreationDTO);

    BookingOutputDto changeApproveBooking(Integer bookingId, Integer userId, boolean approved);
}
