package ru.practicum.shareit.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;


@RestControllerAdvice("ru.practicum.shareit")
@Slf4j
public class ApplicationExceptionHandler {


    @ExceptionHandler({
            CommentForbidden.class,
    })
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse catchValidation(final CommentForbidden e) {
        return new ErrorResponse("Item cannot be commented", e.getMessage());
    }

    @ExceptionHandler({
            ValidationException.class,
    })
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse catchValidation(final ValidationException e) {
        return new ErrorResponse("Parameter validation exception", e.getMessage());
    }

    @ExceptionHandler({
            UnknownBookingState.class,
            BookingCouldntBeModified.class,
    })
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse catchValidation(final RuntimeException e) {
        return new ErrorResponse("Unknown state: " + e.getMessage(),
                "Incorrect booking state was send.");
    }

    @ExceptionHandler({
            UserNotFoundException.class,
            ItemNotFoundException.class,
            BookingNotFoundException.class,
            ItemRequestNotFoundException.class,
            BookingAccessDeniedException.class,
            BookingStatusCanChaneOnlyOwner.class
    })
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse catchNotFound(final RuntimeException e) {
        return new ErrorResponse("Not found exception", e.getMessage());
    }

    @ExceptionHandler({
            ItemCouldntBeModified.class,
    })
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ErrorResponse catchCouldNotModifiedItem(final RuntimeException e) {
        return new ErrorResponse("You can not modify this item", e.getMessage());
    }

    @ExceptionHandler({
            NonUniqueEmail.class
    })
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse catchNonUniqueEmailCreating(final RuntimeException e) {
        return new ErrorResponse("User with such email already exist", e.getMessage());
    }

}
