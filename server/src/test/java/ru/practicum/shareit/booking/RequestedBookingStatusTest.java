package ru.practicum.shareit.booking;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class RequestedBookingStatusTest {

    @Test
    void values() {
        Assertions.assertEquals(RequestedBookingStatus.ALL, RequestedBookingStatus.valueOf("All".toUpperCase()));
        Assertions.assertEquals(RequestedBookingStatus.REJECTED, RequestedBookingStatus.valueOf("REJECTED".toUpperCase()));
        Assertions.assertEquals(RequestedBookingStatus.WAITING, RequestedBookingStatus.valueOf("WAITING".toUpperCase()));
        Assertions.assertEquals(RequestedBookingStatus.PAST, RequestedBookingStatus.valueOf("PAST".toUpperCase()));
        Assertions.assertEquals(RequestedBookingStatus.FUTURE, RequestedBookingStatus.valueOf("FUTURE".toUpperCase()));
        Assertions.assertEquals(RequestedBookingStatus.CURRENT, RequestedBookingStatus.valueOf("CURRENT".toUpperCase()));
    }
}