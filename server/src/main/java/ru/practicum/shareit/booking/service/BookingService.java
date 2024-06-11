package ru.practicum.shareit.booking.service;

import ru.practicum.shareit.booking.dto.BookingCreationDTO;
import ru.practicum.shareit.booking.dto.BookingOutputDto;

import java.util.List;


public interface BookingService {
    BookingOutputDto createBooking(BookingCreationDTO bookingCreationDTO, Integer bookerUserId);

    BookingOutputDto changeApproveBooking(Integer bookingId, Integer approverUserId, boolean approved);

    BookingOutputDto getBooking(Integer userId, Integer bookingId);

    List<BookingOutputDto> findAllBookerBookingsWithState(Integer bookerId, String state, Integer from, Integer size);

    List<BookingOutputDto> findAllOwnerBookingsWithState(Integer ownerId, String state, Integer from, Integer size);
}
