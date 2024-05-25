package ru.practicum.shareit.booking;

import ru.practicum.shareit.booking.dto.BookingCreationDTO;
import ru.practicum.shareit.booking.dto.BookingOutputDto;


public interface BookingService {
    BookingOutputDto createBooking(BookingCreationDTO bookingCreationDTO, Integer bookerUserId);

    BookingOutputDto changeApproveBooking(Integer bookingId, Integer approverUserId, boolean approved);
}
