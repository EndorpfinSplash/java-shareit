package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.dao.BookingRepository;
import ru.practicum.shareit.booking.dto.BookingCreationDTO;
import ru.practicum.shareit.booking.dto.BookingOutputDto;
import ru.practicum.shareit.exception.*;
import ru.practicum.shareit.item.dao.ItemRepository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.dao.UserRepository;

import java.text.MessageFormat;

@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;

    public BookingOutputDto createBooking(BookingCreationDTO bookingCreationDTO, Integer bookerUserId) {
        User userBooker = userRepository.findById(bookerUserId).orElseThrow(() ->
                new UserNotFoundException(MessageFormat.format("User with id {0} not found", bookerUserId)));

        Integer itemId = bookingCreationDTO.getItemId();
        Item item = itemRepository.findById(itemId).orElseThrow(() ->
                new ItemNotFoundException(MessageFormat.format("Item with id {0} not found", itemId)));

        if (!item.isAvailable()) {
            throw new ValidationException("Item not available!");
        }

        if (!bookingCreationDTO.getStart().isBefore(bookingCreationDTO.getEnd())) {
            throw new ValidationException("End time should be after start time!");
        }

        if (item.getOwner().getId().equals(bookerUserId)) {
            throw new ValidationException("You are owner of this item!");
        }

        Booking booking = BookingMapper.toBooking(bookingCreationDTO, userBooker, item);
        Booking savedBooking = bookingRepository.save(booking);
        return BookingMapper.toOutBookingDto(savedBooking);
    }

    public BookingOutputDto changeApproveBooking(Integer bookingId, Integer approverUserId, boolean approved) {
        Booking booking = bookingRepository.findById(bookingId).orElseThrow(() ->
                new BookingNotFoundException(MessageFormat.format("Booking with id {0} not found", bookingId)));

        User approver = userRepository.findById(approverUserId).orElseThrow(() ->
                new UserNotFoundException(MessageFormat.format("User with id {0} not found", approverUserId)));

        User bookingItemOwner = booking.getItem().getOwner();
        if (!bookingItemOwner.equals(approver)) {
            throw new BookingCouldntBeModified(
                    MessageFormat.format("You cant handle foreign booking {0} which belongs to {1}! ",
                            booking,
                            bookingItemOwner)
            );
        }

        if (!booking.getBookingStatus().equals(BookingStatus.WAITING)) {
            throw new ValidationException("Booking possible only from status waiting!");
        }

        booking.setBookingStatus(approved ? BookingStatus.APPROVED : BookingStatus.REJECTED);
        Booking savedBooking = bookingRepository.save(booking);
        return BookingMapper.toOutBookingDto(savedBooking);
    }
}
