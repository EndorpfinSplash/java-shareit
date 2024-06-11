package ru.practicum.shareit.exception;

public class UnknownBookingState extends RuntimeException {
    public UnknownBookingState(String s) {
        super(s);
    }
}
