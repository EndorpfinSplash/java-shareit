package ru.practicum.shareit.booking.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.BookingMapper;
import ru.practicum.shareit.booking.BookingStatus;
import ru.practicum.shareit.booking.RequestedBookingStatus;
import ru.practicum.shareit.booking.dao.BookingRepository;
import ru.practicum.shareit.booking.dto.BookingCreationDTO;
import ru.practicum.shareit.booking.dto.BookingOutputDto;
import ru.practicum.shareit.exception.*;
import ru.practicum.shareit.item.dao.ItemRepository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.dao.UserRepository;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

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
            throw new BookingNotFoundException("You are owner of this item!");
        }

        Booking booking = BookingMapper.toBooking(bookingCreationDTO, userBooker, item);
        Booking savedBooking = bookingRepository.save(booking);
        return BookingMapper.toOutBookingDto(savedBooking);
    }

    public BookingOutputDto changeApproveBooking(Integer bookingId, Integer approverUserId, boolean approved) {
        Booking booking = bookingRepository.findById(bookingId).orElseThrow(() ->
                new BookingNotFoundException(MessageFormat.format("Booking with id {0} not found", bookingId)));

        User booker = userRepository.findById(booking.getBooker().getId()).orElseThrow(() ->
                new BookingNotFoundException(MessageFormat.format("Booker with id {0} not found", booking.getBooker().getId())));

        User approver = userRepository.findById(approverUserId).orElseThrow(() ->
                new UserNotFoundException(MessageFormat.format("User with id {0} not found", approverUserId)));
        if (approver.equals(booker)) {
            throw new BookingStatusCanChaneOnlyOwner("Only owner can modify item!");
        }

        User bookingItemOwner = booking.getItem().getOwner();
        if (!bookingItemOwner.equals(approver)) {
            throw new BookingCouldntBeModified("You cant handle foreign booking!");
        }

        if (!booking.getBookingStatus().equals(BookingStatus.WAITING)) {
            throw new ValidationException("Booking possible only from status waiting!");
        }

        booking.setBookingStatus(approved ? BookingStatus.APPROVED : BookingStatus.REJECTED);
        Booking savedBooking = bookingRepository.save(booking);
        return BookingMapper.toOutBookingDto(savedBooking);
    }

    @Override
    public BookingOutputDto getBooking(Integer userId, Integer bookingId) {
        userRepository.findById(userId).orElseThrow(() ->
                new UserNotFoundException(MessageFormat.format("User with id {0} not found", userId)));
        Booking booking = bookingRepository.findById(bookingId).orElseThrow(() ->
                new BookingNotFoundException(MessageFormat.format("Booking with id {0} not found", bookingId)));
        User itemOwner = booking.getItem().getOwner();
        User booker = booking.getBooker();
        if (!Objects.equals(userId, itemOwner.getId()) && !Objects.equals(userId, booker.getId())) {
            throw new BookingAccessDeniedException(String.format(
                    "Booking with id=%s could be read only by owner or booker", bookingId));
        }
        return BookingMapper.toOutBookingDto(booking);
    }

    @Override
    public List<BookingOutputDto> findAllBookerBookingsWithState(Integer bookerId, String state, Integer from, Integer size) {

        userRepository.findById(bookerId).orElseThrow(() ->
                new UserNotFoundException(MessageFormat.format("Booker with id {0} not found", bookerId)));
        List<Booking> res = new ArrayList<>();

        RequestedBookingStatus reqStatus;
        try {
            reqStatus = RequestedBookingStatus.valueOf(state.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new UnknownBookingState(state);
        }

//        Sort sortByStartDesc = Sort.by(Sort.Direction.DESC, "start");
        Pageable page = PageRequest.of(from > 0 ? from / size : 0, size);
        switch (reqStatus) {
            case ALL:
                res = bookingRepository.findByBooker_IdOrderByStartDesc(bookerId, page);
                break;
            case WAITING:
            case REJECTED:
                res = bookingRepository.findByBooker_IdAndBookingStatusOrderByStartDesc(
                        bookerId,
                        BookingStatus.valueOf(reqStatus.name()),
                        page
                );
                break;
            case CURRENT:
                res = bookingRepository.findByBooker_CurrentBookingsOrderByStartDesc(bookerId, page);
                break;
            case PAST:
                res = bookingRepository.findByBooker_PastBookingsOrderByStartDesc(bookerId, page);
                break;
            case FUTURE:
                res = bookingRepository.findByBooker_FutureBookingsOrderByStartDesc(bookerId, page);
        }
        return res.stream()
                .map(BookingMapper::toOutBookingDto)
                .collect(Collectors.toList());
    }


    @Override
    public List<BookingOutputDto> findAllOwnerBookingsWithState(Integer ownerId, String state, Integer from, Integer size) {

        userRepository.findById(ownerId).orElseThrow(() ->
                new UserNotFoundException(MessageFormat.format("Owner with id {0} not found", ownerId)));
        List<Booking> res = new ArrayList<>();

        RequestedBookingStatus reqStatus;
        try {
            reqStatus = RequestedBookingStatus.valueOf(state.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new UnknownBookingState(state);
        }
        Pageable page = PageRequest.of(from > 0 ? from / size : 0, size);
        switch (reqStatus) {
            case ALL:
                res = bookingRepository.findAllByOwner(ownerId, page);
                break;
            case WAITING:
            case REJECTED:
                res = bookingRepository.findByOwnerAndStatus(
                        ownerId,
                        BookingStatus.valueOf(reqStatus.name()),
                        page
                );
                break;
            case CURRENT:
                res = bookingRepository.findByOwnerCurrentBookingsOrderByStartDesc(ownerId, page);
                break;
            case PAST:
                res = bookingRepository.findByOwnerPast(ownerId, page);
                break;
            case FUTURE:
                res = bookingRepository.findByOwnerFuture(ownerId, page);
        }
        return res.stream()
                .map(BookingMapper::toOutBookingDto)
                .collect(Collectors.toList());
    }
}
