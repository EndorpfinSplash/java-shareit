package ru.practicum.shareit.exception;

public class BookingAccessDeniedException extends RuntimeException {
    public BookingAccessDeniedException(String s) {
        super(s);
    }
}
