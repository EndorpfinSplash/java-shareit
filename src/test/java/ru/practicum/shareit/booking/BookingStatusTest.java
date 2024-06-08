package ru.practicum.shareit.booking;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class BookingStatusTest {

    @Test
    void values() {
        Assertions.assertEquals(BookingStatus.WAITING, BookingStatus.valueOf("WAITING"));
        Assertions.assertEquals(BookingStatus.APPROVED, BookingStatus.valueOf("approved".toUpperCase()));
        Assertions.assertEquals(BookingStatus.REJECTED, BookingStatus.valueOf("REJECTED".toUpperCase()));
        Assertions.assertEquals(BookingStatus.CANCELED, BookingStatus.valueOf("CANCELED".toUpperCase()));
    }

    @Test
    void valueOf() {
    }
}