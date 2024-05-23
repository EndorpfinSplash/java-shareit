package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.dao.BookingRepository;
import ru.practicum.shareit.booking.dto.BookingCreationDTO;
import ru.practicum.shareit.booking.dto.BookingOutputDto;

@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;

    public BookingOutputDto createBooking(BookingCreationDTO bookingCreationDTO) {
        return null;
    }

    public BookingOutputDto changeApproveBooking(Integer bookingId, Integer userId, boolean approved) {
        return null;
    }
}
