package ru.practicum.shareit.booking.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.BookingMapper;
import ru.practicum.shareit.booking.BookingStatus;
import ru.practicum.shareit.booking.RequestedBookingStatus;
import ru.practicum.shareit.booking.dao.BookingRepository;
import ru.practicum.shareit.booking.dto.BookingCreationDTO;
import ru.practicum.shareit.exception.*;
import ru.practicum.shareit.item.dao.ItemRepository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.dao.UserRepository;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookingServiceImplTest {

    @Mock
    private BookingRepository bookingRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private ItemRepository itemRepository;
    @InjectMocks
    private BookingServiceImpl bookingService;

    private User booker;
    private User ownerItem;
    private Item testItem;

    @BeforeEach
    void setUp() {
        booker = new User(1, "Tester", "Tester@mail.com");
        ownerItem = new User(2, "Test Item Owner ", "Tester@mail.com");
        testItem = new Item(3, "Test item", "Item description", true, ownerItem, null);
    }

    @Test
    void createBooking() {
        when(userRepository.findById(booker.getId())).thenReturn(Optional.of(booker));
        when(userRepository.findById(ownerItem.getId())).thenReturn(Optional.of(ownerItem));
        when(itemRepository.findById(testItem.getId())).thenReturn(Optional.of(testItem));
        when(bookingRepository.save(any(Booking.class))).thenAnswer(invocationOnMock -> invocationOnMock.getArgument(0));

        BookingCreationDTO bookingCreationDTO = BookingCreationDTO.builder()
                .itemId(testItem.getId())
                .start(LocalDateTime.now())
                .end(LocalDateTime.now().plusDays(1))
                .build();

        Assertions.assertThrows(BookingNotFoundException.class,
                () -> bookingService.createBooking(bookingCreationDTO, ownerItem.getId()));

        Booking booking = BookingMapper.toBooking(bookingCreationDTO, booker, testItem);
        bookingService.createBooking(bookingCreationDTO, booker.getId());
        verify(bookingRepository, times(1)).save(booking);
    }

    @Test
    void changeApproveBooking() {
        BookingCreationDTO bookingCreationDTO = BookingCreationDTO.builder()
                .itemId(testItem.getId())
                .start(LocalDateTime.now())
                .end(LocalDateTime.now().plusDays(1))
                .build();
        Booking booking = BookingMapper.toBooking(bookingCreationDTO, booker, testItem);
        booking.setId(10);
        when(bookingRepository.findById(any(Integer.class))).thenReturn(Optional.of(booking));
        when(userRepository.findById(booker.getId())).thenReturn(Optional.of(booker));
        when(userRepository.findById(ownerItem.getId())).thenReturn(Optional.of(ownerItem));
        when(bookingRepository.save(any(Booking.class))).thenAnswer(invocationOnMock -> invocationOnMock.getArgument(0));


        bookingService.changeApproveBooking(10, ownerItem.getId(), true);
        verify(bookingRepository, times(1)).save(booking);

        Assertions.assertThrows(BookingStatusCanChaneOnlyOwner.class, () ->
                bookingService.changeApproveBooking(10, booker.getId(), true));

        booking.setBookingStatus(BookingStatus.REJECTED);

        Assertions.assertThrows(ValidationException.class, () ->
                bookingService.changeApproveBooking(10, ownerItem.getId(), true));
    }

    @Test
    void getBooking() {
        BookingCreationDTO bookingCreationDTO = BookingCreationDTO.builder()
                .itemId(testItem.getId())
                .start(LocalDateTime.now())
                .end(LocalDateTime.now().plusDays(1))
                .build();
        Booking booking = BookingMapper.toBooking(bookingCreationDTO, booker, testItem);
        booking.setId(20);
        when(userRepository.findById(ownerItem.getId())).thenReturn(Optional.of(ownerItem));
        when(bookingRepository.findById(20)).thenReturn(Optional.of(booking));
        bookingService.getBooking(ownerItem.getId(), 20);
        verify(bookingRepository, times(1)).findById(any(Integer.class));

        Assertions.assertThrows(BookingNotFoundException.class, () ->
                bookingService.getBooking(ownerItem.getId(), 200));
        Assertions.assertThrows(UserNotFoundException.class, () ->
                bookingService.getBooking(300, 200));

        when(userRepository.findById(400)).thenReturn(Optional.of(
                new User(400, "Anoter User", "another@tt.com")));
        Assertions.assertThrows(BookingAccessDeniedException.class, () ->
                bookingService.getBooking(400, 20));
    }

    @Test
    void findAllBookerBookingsWithState() {
        when(userRepository.findById(booker.getId())).thenReturn(Optional.of(booker));

        Assertions.assertThrows(UnknownBookingState.class, () ->
                bookingService.findAllBookerBookingsWithState(booker.getId(), "WrongState", 0, 10));

        Pageable page = PageRequest.of(0, 10);
        bookingService.findAllBookerBookingsWithState(booker.getId(), RequestedBookingStatus.ALL.name(), 0, 10);
        verify(bookingRepository, times(1)).findByBooker_IdOrderByStartDesc(booker.getId(), page);

        bookingService.findAllBookerBookingsWithState(booker.getId(), RequestedBookingStatus.WAITING.name(), 0, 10);
        verify(bookingRepository, times(1)).findByBooker_IdAndBookingStatusOrderByStartDesc(
                booker.getId(),
                BookingStatus.valueOf(RequestedBookingStatus.WAITING.name()),
                page
        );
        bookingService.findAllBookerBookingsWithState(booker.getId(), RequestedBookingStatus.REJECTED.name(), 0, 10);
        verify(bookingRepository, times(1)).findByBooker_IdAndBookingStatusOrderByStartDesc(
                booker.getId(),
                BookingStatus.valueOf(RequestedBookingStatus.REJECTED.name()),
                page
        );

        bookingService.findAllBookerBookingsWithState(booker.getId(), RequestedBookingStatus.CURRENT.name(), 0, 10);
        verify(bookingRepository, times(1)).findByBooker_CurrentBookingsOrderByStartDesc(
                booker.getId(),
                page
        );

        bookingService.findAllBookerBookingsWithState(booker.getId(), RequestedBookingStatus.PAST.name(), 0, 10);
        verify(bookingRepository, times(1)).findByBooker_PastBookingsOrderByStartDesc(
                booker.getId(),
                page
        );

        bookingService.findAllBookerBookingsWithState(booker.getId(), RequestedBookingStatus.FUTURE.name(), 0, 10);
        verify(bookingRepository, times(1)).findByBooker_FutureBookingsOrderByStartDesc(
                booker.getId(),
                page
        );
    }

    @Test
    void findAllOwnerBookingsWithState() {
        when(userRepository.findById(ownerItem.getId())).thenReturn(Optional.of(ownerItem));

        Assertions.assertThrows(UnknownBookingState.class, () ->
                bookingService.findAllOwnerBookingsWithState(ownerItem.getId(), "WrongState", 0, 10));

        Pageable page = PageRequest.of(0, 10);
        bookingService.findAllOwnerBookingsWithState(ownerItem.getId(), RequestedBookingStatus.ALL.name(), 0, 10);
        verify(bookingRepository, times(1)).findAllByOwner(ownerItem.getId(), page);

        bookingService.findAllOwnerBookingsWithState(ownerItem.getId(), RequestedBookingStatus.WAITING.name(), 0, 10);
        verify(bookingRepository, times(1)).findByOwnerAndStatus(
                ownerItem.getId(),
                BookingStatus.valueOf(RequestedBookingStatus.WAITING.name()),
                page
        );
        bookingService.findAllOwnerBookingsWithState(ownerItem.getId(), RequestedBookingStatus.REJECTED.name(), 0, 10);
        verify(bookingRepository, times(1)).findByOwnerAndStatus(
                ownerItem.getId(),
                BookingStatus.valueOf(RequestedBookingStatus.REJECTED.name()),
                page
        );

        bookingService.findAllOwnerBookingsWithState(ownerItem.getId(), RequestedBookingStatus.CURRENT.name(), 0, 10);
        verify(bookingRepository, times(1)).findByOwnerCurrentBookingsOrderByStartDesc(
                ownerItem.getId(),
                page
        );

        bookingService.findAllOwnerBookingsWithState(ownerItem.getId(), RequestedBookingStatus.PAST.name(), 0, 10);
        verify(bookingRepository, times(1)).findByOwnerPast(
                ownerItem.getId(),
                page
        );

        bookingService.findAllOwnerBookingsWithState(ownerItem.getId(), RequestedBookingStatus.FUTURE.name(), 0, 10);
        verify(bookingRepository, times(1)).findByOwnerFuture(
                ownerItem.getId(),
                page
        );
    }
}