package ru.practicum.shareit.booking.dao;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.jdbc.Sql;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.BookingStatus;
import ru.practicum.shareit.booking.dto.ShortBookingView;
import ru.practicum.shareit.item.dao.ItemRepository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.dao.UserRepository;

import java.time.LocalDateTime;
import java.util.List;

@DataJpaTest
@Sql(scripts = "/schema-test.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
class BookingRepositoryTest {

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ItemRepository itemRepository;

    private User booker;
    private User itemOwner;
    private Item bookedItem;


    @BeforeEach
    void setUp() {
        User bookerForSave = User.builder()
                .name("Test Booker user")
                .email("booker@test.com")
                .build();
        booker = userRepository.save(bookerForSave);
        User itemOwnerForSave = User.builder()
                .name("Test itemOwner user")
                .email("itemOwner@test.com")
                .build();
        itemOwner = userRepository.save(itemOwnerForSave);
        Item bookedItemForSave = Item.builder()
                .name("Test booked item")
                .description("Test booked item description")
                .owner(itemOwner)
                .available(true)
                .build();
        bookedItem = itemRepository.save(bookedItemForSave);
    }

    @Test
    void countFinishedItemBookingsByBooker() {
        Booking finishedBooking = Booking.builder()
                .booker(booker)
                .item(bookedItem)
                .start(LocalDateTime.now().minusDays(11))
                .end(LocalDateTime.now().minusDays(10))
                .bookingStatus(BookingStatus.APPROVED)
                .build();
        bookingRepository.save(finishedBooking);

        Booking finishedBooking2 = Booking.builder()
                .booker(booker)
                .item(bookedItem)
                .start(LocalDateTime.now().plusHours(5))
                .end(LocalDateTime.now().plusDays(10))
                .bookingStatus(BookingStatus.APPROVED)
                .build();
        bookingRepository.save(finishedBooking2);

        Integer cntFinishedBookings = bookingRepository.countFinishedItemBookingsByBooker(
                booker.getId(),
                bookedItem.getId(),
                BookingStatus.APPROVED,
                LocalDateTime.now()
        );
        Assertions.assertEquals(1, cntFinishedBookings);
    }

    @Test
    void findByBooker_IdOrderByStartDesc() {
        Booking booking1 = Booking.builder()
                .booker(booker)
                .item(bookedItem)
                .start(LocalDateTime.now().plusDays(1))
                .end(LocalDateTime.now().plusDays(10))
                .bookingStatus(BookingStatus.WAITING)
                .build();
        bookingRepository.save(booking1);

        Booking booking2 = Booking.builder()
                .booker(booker)
                .item(bookedItem)
                .start(LocalDateTime.now().plusDays(15))
                .end(LocalDateTime.now().plusDays(20))
                .bookingStatus(BookingStatus.WAITING)
                .build();
        bookingRepository.save(booking2);

        List<Booking> bookerBookingList = bookingRepository.findByBooker_IdOrderByStartDesc(
                booker.getId(),
                PageRequest.of(0, 10)
        );
        Assertions.assertEquals(2, bookerBookingList.size());
        Assertions.assertEquals(booking2.getId(), bookerBookingList.get(0).getId());
        Assertions.assertEquals(booking1.getId(), bookerBookingList.get(1).getId());
    }

    @Test
    void findByBooker_IdAndBookingStatusOrderByStartDesc() {
        Booking booking1 = Booking.builder()
                .booker(booker)
                .item(bookedItem)
                .start(LocalDateTime.now().plusDays(1))
                .end(LocalDateTime.now().plusDays(10))
                .bookingStatus(BookingStatus.CANCELED)
                .build();
        bookingRepository.save(booking1);

        Booking booking2 = Booking.builder()
                .booker(booker)
                .item(bookedItem)
                .start(LocalDateTime.now().plusDays(15))
                .end(LocalDateTime.now().plusDays(20))
                .bookingStatus(BookingStatus.CANCELED)
                .build();
        bookingRepository.save(booking2);

        List<Booking> bookerBookingList = bookingRepository.findByBooker_IdAndBookingStatusOrderByStartDesc(
                booker.getId(),
                BookingStatus.CANCELED,
                PageRequest.of(0, 10)
        );
        Assertions.assertEquals(2, bookerBookingList.size());
        Assertions.assertEquals(booking2.getId(), bookerBookingList.get(0).getId());
        Assertions.assertEquals(booking1.getId(), bookerBookingList.get(1).getId());
    }

    @Test
    void findByBooker_CurrentBookingsOrderByStartDesc() {
        Booking booking1 = Booking.builder()
                .booker(booker)
                .item(bookedItem)
                .start(LocalDateTime.now().minusDays(1))
                .end(LocalDateTime.now().plusDays(10))
                .bookingStatus(BookingStatus.WAITING)
                .build();
        bookingRepository.save(booking1);

        Item bookedItemForSave = Item.builder()
                .name("Test booked item 2")
                .description("Test booked item description 2")
                .owner(itemOwner)
                .available(true)
                .build();
        Item savedItem2 = itemRepository.save(bookedItemForSave);

        Booking booking2 = Booking.builder()
                .booker(booker)
                .item(savedItem2)
                .start(LocalDateTime.now().minusDays(15))
                .end(LocalDateTime.now().plusDays(20))
                .bookingStatus(BookingStatus.WAITING)
                .build();
        bookingRepository.save(booking2);

        List<Booking> bookerBookingList = bookingRepository.findByBooker_CurrentBookingsOrderByStartDesc(
                booker.getId(),
                PageRequest.of(0, 10)
        );
        Assertions.assertEquals(2, bookerBookingList.size());
        Assertions.assertEquals(booking1.getId(), bookerBookingList.get(0).getId());
        Assertions.assertEquals(booking2.getId(), bookerBookingList.get(1).getId());
    }

    @Test
    void findByBooker_PastBookingsOrderByStartDesc() {

        Booking booking1 = Booking.builder()
                .booker(booker)
                .item(bookedItem)
                .start(LocalDateTime.now().minusDays(11))
                .end(LocalDateTime.now().minusDays(10))
                .bookingStatus(BookingStatus.CANCELED)
                .build();
        bookingRepository.save(booking1);

        Booking booking2 = Booking.builder()
                .booker(booker)
                .item(bookedItem)
                .start(LocalDateTime.now().minusDays(35))
                .end(LocalDateTime.now().minusDays(20))
                .bookingStatus(BookingStatus.APPROVED)
                .build();
        bookingRepository.save(booking2);

        List<Booking> bookerBookingList = bookingRepository.findByBooker_PastBookingsOrderByStartDesc(
                booker.getId(),
                PageRequest.of(0, 10)
        );
        Assertions.assertEquals(2, bookerBookingList.size());
        Assertions.assertEquals(booking1.getId(), bookerBookingList.get(0).getId());
        Assertions.assertEquals(booking2.getId(), bookerBookingList.get(1).getId());
    }

    @Test
    void findByBooker_FutureBookingsOrderByStartDesc() {
        Booking booking1 = Booking.builder()
                .booker(booker)
                .item(bookedItem)
                .start(LocalDateTime.now().plusDays(1))
                .end(LocalDateTime.now().plusDays(10))
                .bookingStatus(BookingStatus.WAITING)
                .build();
        bookingRepository.save(booking1);

        Booking booking2 = Booking.builder()
                .booker(booker)
                .item(bookedItem)
                .start(LocalDateTime.now().plusDays(15))
                .end(LocalDateTime.now().plusDays(20))
                .bookingStatus(BookingStatus.WAITING)
                .build();
        bookingRepository.save(booking2);

        List<Booking> bookerBookings = bookingRepository.findByBooker_FutureBookingsOrderByStartDesc(
                booker.getId(),
                PageRequest.of(0, 10)
        );
        Assertions.assertEquals(2, bookerBookings.size());
        Assertions.assertEquals(booking2.getId(), bookerBookings.get(0).getId());
        Assertions.assertEquals(booking1.getId(), bookerBookings.get(1).getId());
    }

    @Test
    void findAllByOwner() {
        Booking booking1 = Booking.builder()
                .booker(booker)
                .item(bookedItem)
                .start(LocalDateTime.now().plusDays(1))
                .end(LocalDateTime.now().plusDays(10))
                .bookingStatus(BookingStatus.WAITING)
                .build();
        Booking savedBooking1 = bookingRepository.save(booking1);

        Booking booking2 = Booking.builder()
                .booker(booker)
                .item(bookedItem)
                .start(LocalDateTime.now().plusDays(15))
                .end(LocalDateTime.now().plusDays(20))
                .bookingStatus(BookingStatus.WAITING)
                .build();
        Booking savedBooking2 = bookingRepository.save(booking2);

        List<Booking> allByOwnerBookings = bookingRepository.findAllByOwner(
                itemOwner.getId(),
                PageRequest.of(0, 10)
        );
        Assertions.assertEquals(2, allByOwnerBookings.size());
        Assertions.assertTrue(allByOwnerBookings.contains(savedBooking1));
        Assertions.assertTrue(allByOwnerBookings.contains(savedBooking2));
    }

    @Test
    void findByOwnerAndStatus() {
        Booking booking1 = Booking.builder()
                .booker(booker)
                .item(bookedItem)
                .start(LocalDateTime.now().plusDays(1))
                .end(LocalDateTime.now().plusDays(10))
                .bookingStatus(BookingStatus.REJECTED)
                .build();
        Booking savedBooking1 = bookingRepository.save(booking1);

        Booking booking2 = Booking.builder()
                .booker(booker)
                .item(bookedItem)
                .start(LocalDateTime.now().plusDays(15))
                .end(LocalDateTime.now().plusDays(20))
                .bookingStatus(BookingStatus.REJECTED)
                .build();
        Booking savedBooking2 = bookingRepository.save(booking2);

        Booking booking3 = Booking.builder()
                .booker(booker)
                .item(bookedItem)
                .start(LocalDateTime.now().plusDays(55))
                .end(LocalDateTime.now().plusDays(70))
                .bookingStatus(BookingStatus.CANCELED)
                .build();
        Booking savedBooking3 = bookingRepository.save(booking3);

        List<Booking> allByOwnerBookingsRejected = bookingRepository.findByOwnerAndStatus(
                itemOwner.getId(),
                BookingStatus.REJECTED,
                PageRequest.of(0, 10)
        );
        Assertions.assertEquals(2, allByOwnerBookingsRejected.size());
        Assertions.assertTrue(allByOwnerBookingsRejected.contains(savedBooking1));
        Assertions.assertTrue(allByOwnerBookingsRejected.contains(savedBooking2));

        List<Booking> allByOwnerBookingsCanceled = bookingRepository.findByOwnerAndStatus(
                itemOwner.getId(),
                BookingStatus.CANCELED,
                PageRequest.of(0, 10)
        );
        Assertions.assertEquals(1, allByOwnerBookingsCanceled.size());
        Assertions.assertTrue(allByOwnerBookingsCanceled.contains(savedBooking3));
    }

    @Test
    void findByOwnerCurrentBookingsOrderByStartDesc() {
        Booking booking1 = Booking.builder()
                .booker(booker)
                .item(bookedItem)
                .start(LocalDateTime.now().minusDays(11))
                .end(LocalDateTime.now().plusDays(10))
                .bookingStatus(BookingStatus.APPROVED)
                .build();
        Booking savedBooking1 = bookingRepository.save(booking1);

        Item bookedItemForSave = Item.builder()
                .name("Test booked item 2")
                .description("Test booked item description 2")
                .owner(itemOwner)
                .available(true)
                .build();
        Item savedItem2 = itemRepository.save(bookedItemForSave);

        Booking booking2 = Booking.builder()
                .booker(booker)
                .item(savedItem2)
                .start(LocalDateTime.now().minusDays(15))
                .end(LocalDateTime.now().plusDays(20))
                .bookingStatus(BookingStatus.REJECTED)
                .build();
        Booking savedBooking2 = bookingRepository.save(booking2);

        List<Booking> byOwnerCurrentBookingsOrderByStartDesc = bookingRepository.findByOwnerCurrentBookingsOrderByStartDesc(
                itemOwner.getId(),
                PageRequest.of(0, 10)
        );
        Assertions.assertEquals(2, byOwnerCurrentBookingsOrderByStartDesc.size());
        Assertions.assertEquals(byOwnerCurrentBookingsOrderByStartDesc.get(0), savedBooking1);
        Assertions.assertEquals(byOwnerCurrentBookingsOrderByStartDesc.get(1), savedBooking2);
    }

    @Test
    void findByOwnerPast() {
        Booking booking1 = Booking.builder()
                .booker(booker)
                .item(bookedItem)
                .start(LocalDateTime.now().minusDays(11))
                .end(LocalDateTime.now().plusDays(10))
                .bookingStatus(BookingStatus.APPROVED)
                .build();
        Booking savedBooking1 = bookingRepository.save(booking1);

        Item bookedItemForSave = Item.builder()
                .name("Test booked item 2")
                .description("Test booked item description 2")
                .owner(itemOwner)
                .available(true)
                .build();
        Item savedItem2 = itemRepository.save(bookedItemForSave);

        Booking booking2 = Booking.builder()
                .booker(booker)
                .item(savedItem2)
                .start(LocalDateTime.now().minusDays(15))
                .end(LocalDateTime.now().plusDays(20))
                .bookingStatus(BookingStatus.REJECTED)
                .build();
        Booking savedBooking2 = bookingRepository.save(booking2);

        List<Booking> byOwnerCurrentBookingsOrderByStartDesc = bookingRepository.findByOwnerCurrentBookingsOrderByStartDesc(
                itemOwner.getId(),
                PageRequest.of(0, 10)
        );
        Assertions.assertEquals(2, byOwnerCurrentBookingsOrderByStartDesc.size());
        Assertions.assertEquals(byOwnerCurrentBookingsOrderByStartDesc.get(0), savedBooking1);
        Assertions.assertEquals(byOwnerCurrentBookingsOrderByStartDesc.get(1), savedBooking2);
    }

    @Test
    void findByOwnerFuture() {
        Booking booking1 = Booking.builder()
                .booker(booker)
                .item(bookedItem)
                .start(LocalDateTime.now().plusDays(3))
                .end(LocalDateTime.now().plusDays(10))
                .bookingStatus(BookingStatus.APPROVED)
                .build();
        Booking savedBooking1 = bookingRepository.save(booking1);

        Booking booking2 = Booking.builder()
                .booker(booker)
                .item(bookedItem)
                .start(LocalDateTime.now().plusDays(1))
                .end(LocalDateTime.now().plusDays(2))
                .bookingStatus(BookingStatus.REJECTED)
                .build();
        Booking savedBooking2 = bookingRepository.save(booking2);

        List<Booking> byOwnerFuture = bookingRepository.findByOwnerFuture(
                itemOwner.getId(),
                PageRequest.of(0, 10)
        );
        Assertions.assertEquals(2, byOwnerFuture.size());
        Assertions.assertEquals(byOwnerFuture.get(0), savedBooking1);
        Assertions.assertEquals(byOwnerFuture.get(1), savedBooking2);
    }

    @Test
    void findLastItemBooking() {
        Booking booking1 = Booking.builder()
                .booker(booker)
                .item(bookedItem)
                .start(LocalDateTime.now().minusDays(5))
                .end(LocalDateTime.now().minusDays(1))
                .bookingStatus(BookingStatus.APPROVED)
                .build();
        bookingRepository.save(booking1);

        Booking booking2 = Booking.builder()
                .booker(booker)
                .item(bookedItem)
                .start(LocalDateTime.now().minusDays(11))
                .end(LocalDateTime.now().minusDays(7))
                .bookingStatus(BookingStatus.REJECTED)
                .build();
        bookingRepository.save(booking2);

        ShortBookingView lastItemBooking = bookingRepository.findLastItemBooking(
                bookedItem.getId()
        );
        Assertions.assertEquals(1, lastItemBooking.getId());
    }

    @Test
    void findNextItemBooking() {
        Booking booking1 = Booking.builder()
                .booker(booker)
                .item(bookedItem)
                .start(LocalDateTime.now().plusDays(5))
                .end(LocalDateTime.now().plusDays(11))
                .bookingStatus(BookingStatus.APPROVED)
                .build();
        bookingRepository.save(booking1);

        Booking booking2 = Booking.builder()
                .booker(booker)
                .item(bookedItem)
                .start(LocalDateTime.now().minusDays(12))
                .end(LocalDateTime.now().minusDays(17))
                .bookingStatus(BookingStatus.REJECTED)
                .build();
        bookingRepository.save(booking2);

        ShortBookingView nextItemBooking = bookingRepository.findNextItemBooking(
                bookedItem.getId()
        );
        Assertions.assertEquals(1, nextItemBooking.getId());
    }
}