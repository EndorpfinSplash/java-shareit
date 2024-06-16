package ru.practicum.shareit.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;


@RestControllerAdvice("ru.practicum.shareit")
@Slf4j
public class ApplicationExceptionHandler {


    @ExceptionHandler({
            IllegalArgumentException.class,
    })
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse catchValidation(final RuntimeException e) {
        return new ErrorResponse(e.getMessage(),
                "Incorrect booking state was send.");
    }


    @ExceptionHandler({
            MethodArgumentNotValidException.class,
    })
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse catchBodyValuesValidation(final MethodArgumentNotValidException e) {
        return new ErrorResponse(e.getMessage(),
                "Incorrect field was send in the body.");
    }

}
