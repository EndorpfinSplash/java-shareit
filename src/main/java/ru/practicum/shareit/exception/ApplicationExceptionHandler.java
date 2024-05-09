package ru.practicum.shareit.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice("ru.practicum.shareit")
@Slf4j
public class ApplicationExceptionHandler {


//        @ExceptionHandler({
//                ValidationException.class
//        })
//        @ResponseStatus(HttpStatus.BAD_REQUEST)
//        public ErrorResponse catchValidation(final ValidationException e) {
//            return new ErrorResponse("Parameter validation exception", e.getMessage());
//        }

    @ExceptionHandler({
            UserNotFoundException.class,
            ItemNotFoundException.class,
//                FilmNotFoundException.class,
//                MpaNotFoundException.class,
//                GenreNotFoundException.class
    })
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse catchNotFound(final RuntimeException e) {
        return new ErrorResponse("Not found exception", e.getMessage());
    }

//        @ExceptionHandler
//        @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
//        public ErrorResponse catchValidation(final RuntimeException e) {
//            log.info("Failed with error: {}", e.getStackTrace());
//            return new ErrorResponse("Application error", e.getMessage());
//        }

}
