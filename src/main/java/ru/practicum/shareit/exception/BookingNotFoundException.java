package ru.practicum.shareit.exception;

public class BookingNotFoundException extends RuntimeException {
    public BookingNotFoundException(String s) {
        super(s);
    }
}
