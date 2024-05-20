package ru.practicum.shareit.booking;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingCreationDTO;
import ru.practicum.shareit.booking.dto.BookingOutputDto;
import ru.practicum.shareit.user.dto.UserCreationDTO;
import ru.practicum.shareit.user.dto.UserOutputDto;

import javax.validation.Valid;

/**
 * TODO Sprint add-bookings.
 */
@RestController
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
@Slf4j
public class BookingController {

    private final BookingService bookingService;

    @PostMapping
    public BookingOutputDto createBooking(@Valid @RequestBody BookingCreationDTO bookingCreationDTO) {
//        После создания запрос находится в статусе WAITING — «ожидает подтверждения».
        log.info("POST request to create {} booking.", bookingCreationDTO);
        BookingOutputDto createdBooking = bookingService.createBooking(bookingCreationDTO);
        log.info("{} was created", createdBooking);
        return null;
    }
/*Подтверждение или отклонение запроса на бронирование.
Может быть выполнено только владельцем вещи. Затем статус бронирования становится либо APPROVED, либо REJECTED.
Эндпоинт — PATCH /bookings/{bookingId}?approved={approved},
параметр approved может принимать значения true или false.*/

    @PatchMapping("/{bookingId}")
    public BookingOutputDto approveBooking(@PathVariable Integer bookingId,
                                           @RequestParam(name = "approved", defaultValue = "false") boolean approved,
                                           @RequestHeader("X-Sharer-User-Id") Integer userId) {
        log.info("Patch request to approve booking.");
        BookingOutputDto createdBooking = bookingService.approveBooking(bookingId, userId, approved);
        return null;
    }

}
