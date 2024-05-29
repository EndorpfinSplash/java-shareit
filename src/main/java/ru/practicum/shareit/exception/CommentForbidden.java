package ru.practicum.shareit.exception;

public class CommentForbidden extends RuntimeException {
    public CommentForbidden(String s) {
        super(s);
    }
}
