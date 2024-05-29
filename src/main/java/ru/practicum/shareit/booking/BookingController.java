package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingCreationDTO;
import ru.practicum.shareit.booking.dto.BookingOutputDto;

import javax.validation.Valid;
import java.util.List;

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
    public BookingOutputDto createBooking(@Valid @RequestBody BookingCreationDTO bookingCreationDTO,
                                          @RequestHeader("X-Sharer-User-Id") Integer bookerUserId) {
        log.info("POST request to create {} booking.", bookingCreationDTO);
        BookingOutputDto createdBooking = bookingService.createBooking(bookingCreationDTO, bookerUserId);
        log.info("{} was created", createdBooking);
        return createdBooking;
    }

    @PatchMapping("/{bookingId}")
    public BookingOutputDto changeApproveBooking(@PathVariable Integer bookingId,
                                                 @RequestParam(name = "approved", defaultValue = "false") boolean approved,
                                                 @RequestHeader("X-Sharer-User-Id") Integer approverUserId) {
        log.info("Patch request to approve booking.");
        BookingOutputDto changedBooking = bookingService.changeApproveBooking(bookingId, approverUserId, approved);
        return changedBooking;
    }

    @GetMapping("/{bookingId}")
    public BookingOutputDto getBooking(@PathVariable Integer bookingId,
                                       @RequestHeader("X-Sharer-User-Id") Integer userId) {
        log.info("Get request to get booking info.");
        return bookingService.getBooking(userId, bookingId);
    }

    @GetMapping
    public List<BookingOutputDto> getAllBookerBookings(@RequestParam(name = "state", defaultValue = "ALL") String state,
                                                       @RequestHeader("X-Sharer-User-Id") Integer userId) {
        log.info("Get request to get all booker's bookings info.");
        return bookingService.findAllBookerBookingsWithState(userId, state);
    }

    @GetMapping("/owner")
    public List<BookingOutputDto> getAllOwnerBookingRequestsInState(@RequestParam(name = "state", defaultValue = "ALL") String state,
                                                                    @RequestHeader("X-Sharer-User-Id") Integer ownerId) {
        log.info("Get request to get all owner's bookings info.");
        return bookingService.findAllOwnerBookingsWithState(ownerId, state);
    }

}
