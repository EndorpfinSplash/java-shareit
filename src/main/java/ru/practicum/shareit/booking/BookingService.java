package ru.practicum.shareit.booking;

import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.dto.BookingCreationDTO;
import ru.practicum.shareit.booking.dto.BookingOutputDto;

@Service
public class BookingService {
    public BookingOutputDto createBooking(BookingCreationDTO bookingCreationDTO) {
        return null;
    }

    public BookingOutputDto approveBooking(Integer bookingId, Integer userId, boolean approved) {
        return null;
    }
}
