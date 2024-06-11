package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingCreationDTO;
import ru.practicum.shareit.booking.dto.BookingOutputDto;
import ru.practicum.shareit.booking.service.BookingService;

import javax.validation.Valid;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;


@RestController
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
@Slf4j
@Validated
public class BookingController {

    private final BookingService bookingService;

    @PostMapping
    public BookingOutputDto createBooking(
            @Valid @RequestBody BookingCreationDTO bookingCreationDTO,
            @RequestHeader("X-Sharer-User-Id") Integer bookerUserId
    ) {
        log.info("POST request to create {} booking.", bookingCreationDTO);
        BookingOutputDto createdBooking = bookingService.createBooking(bookingCreationDTO, bookerUserId);
        log.info("{} was created", createdBooking);
        return createdBooking;
    }

    @PatchMapping("/{bookingId}")
    public BookingOutputDto changeApproveBooking(
            @PathVariable Integer bookingId,
            @RequestParam(name = "approved", defaultValue = "false") boolean approved,
            @RequestHeader("X-Sharer-User-Id") Integer approverUserId
    ) {
        log.info("Patch request to approve booking.");
        BookingOutputDto changedBooking = bookingService.changeApproveBooking(bookingId, approverUserId, approved);
        log.info("Booking was updated successfully.");
        return changedBooking;
    }

    @GetMapping("/{bookingId}")
    public BookingOutputDto getBooking(@PathVariable Integer bookingId,
                                       @RequestHeader("X-Sharer-User-Id") Integer userId) {
        log.info("Get request to get booking info.");
        BookingOutputDto booking = bookingService.getBooking(userId, bookingId);
        log.info("Booking with id={} get successfully by user with id ={}.", bookingId, userId);
        return booking;
    }

    @GetMapping
    public List<BookingOutputDto> getAllBookerBookings(
            @RequestParam(name = "state", defaultValue = "ALL") String state,
            @RequestParam(value = "from", defaultValue = "0") @PositiveOrZero Integer from,
            @RequestParam(value = "size", defaultValue = "10") Integer size,
            @RequestHeader("X-Sharer-User-Id") Integer bookerId
    ) {
        log.info("Get request to get all booker's bookings info.");
        List<BookingOutputDto> allBookerBookingsWithState = bookingService.findAllBookerBookingsWithState(
                bookerId,
                state,
                from,
                size);
        log.info("Booker with id={} get his bookings in state={} successfully.", bookerId, state);
        return allBookerBookingsWithState;
    }

    @GetMapping("/owner")
    public List<BookingOutputDto> getAllOwnerBookingRequestsInState(
            @RequestParam(name = "state", defaultValue = "ALL") String state,
            @RequestParam(value = "from", defaultValue = "0") @PositiveOrZero Integer from,
            @RequestParam(value = "size", defaultValue = "10") Integer size,
            @RequestHeader("X-Sharer-User-Id") Integer ownerId
    ) {
        log.info("Get request to get all owner's bookings info.");
        List<BookingOutputDto> allOwnerBookingsWithState = bookingService.findAllOwnerBookingsWithState(
                ownerId,
                state,
                from,
                size
        );
        log.info("Owner with id={} get request for bookings in state={} successfully.", ownerId, state);
        return allOwnerBookingsWithState;
    }

}
