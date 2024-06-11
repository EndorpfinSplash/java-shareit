package ru.practicum.shareit.exception;

public class NonUniqueEmail extends RuntimeException {
    public NonUniqueEmail(String s) {
        super(s);
    }
}
