package ru.practicum.shareit.exception;

public class BookingStatusCanChaneOnlyOwner extends RuntimeException {
    public BookingStatusCanChaneOnlyOwner(String s) {
        super(s);
    }
}
