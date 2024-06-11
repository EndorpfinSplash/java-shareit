package ru.practicum.shareit.exception;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

class ApplicationExceptionHandlerTest {

    private ApplicationExceptionHandler errorHandler;

    @BeforeEach
    void setUp() {
        errorHandler = new ApplicationExceptionHandler();
    }

    @Test
    void catchValidation() {
        ValidationException testValidationException = new ValidationException("test validation Exception");
        ErrorResponse resultErrorResponse = errorHandler.catchValidation(testValidationException);
        assertNotNull(resultErrorResponse);
        Assertions.assertEquals(testValidationException.getMessage(), resultErrorResponse.getDescription());
    }

    @Test
    void testCatchValidation() {
        CommentForbidden testCommentForbiddenException = new CommentForbidden("test CommentForbidden Exception");
        ErrorResponse resultErrorResponse = errorHandler.catchValidation(testCommentForbiddenException);
        assertNotNull(resultErrorResponse);
        Assertions.assertEquals(testCommentForbiddenException.getMessage(), resultErrorResponse.getDescription());
    }

    @Test
    void testCatchValidation1() {
        UnknownBookingState testUnknownBookingStateException = new UnknownBookingState("Incorrect booking state was send.");
        ErrorResponse resultErrorResponse = errorHandler.catchValidation(testUnknownBookingStateException);
        assertNotNull(resultErrorResponse);
        Assertions.assertEquals(testUnknownBookingStateException.getMessage(), resultErrorResponse.getDescription());

        BookingCouldntBeModified testBookingCouldntBeModifiedException = new BookingCouldntBeModified("Incorrect booking state was send.");
        resultErrorResponse = errorHandler.catchValidation(testBookingCouldntBeModifiedException);
        assertNotNull(resultErrorResponse);
        Assertions.assertEquals(testUnknownBookingStateException.getMessage(), resultErrorResponse.getDescription());
    }

    @Test
    void catchNotFound() {
        UserNotFoundException testUserNotFoundException = new UserNotFoundException("User not found ");
        ErrorResponse resultErrorResponse = errorHandler.catchNotFound(testUserNotFoundException);
        assertNotNull(resultErrorResponse);
        Assertions.assertEquals(testUserNotFoundException.getMessage(), resultErrorResponse.getDescription());

        var testItemNotFoundException = new ItemNotFoundException("Item not found ");
        resultErrorResponse = errorHandler.catchNotFound(testItemNotFoundException);
        assertNotNull(resultErrorResponse);
        Assertions.assertEquals(testItemNotFoundException.getMessage(), resultErrorResponse.getDescription());

        var testNotFoundException = new BookingNotFoundException("Booking not found ");
        resultErrorResponse = errorHandler.catchNotFound(testNotFoundException);
        assertNotNull(resultErrorResponse);
        Assertions.assertEquals(testNotFoundException.getMessage(), resultErrorResponse.getDescription());

        var testItemRequestNotFoundException = new ItemRequestNotFoundException("Item Request not found ");
        resultErrorResponse = errorHandler.catchNotFound(testItemRequestNotFoundException);
        assertNotNull(resultErrorResponse);
        Assertions.assertEquals(testItemRequestNotFoundException.getMessage(), resultErrorResponse.getDescription());

        var testBookingRequestNotFoundException = new BookingAccessDeniedException("Booking Request deny ");
        resultErrorResponse = errorHandler.catchNotFound(testBookingRequestNotFoundException);
        assertNotNull(resultErrorResponse);
        Assertions.assertEquals(testBookingRequestNotFoundException.getMessage(), resultErrorResponse.getDescription());

        var testBookingStatusCanChaneOnlyOwnerException = new BookingStatusCanChaneOnlyOwner("Booking Status Can Chane Only Owner ");
        resultErrorResponse = errorHandler.catchNotFound(testBookingStatusCanChaneOnlyOwnerException);
        assertNotNull(resultErrorResponse);
        Assertions.assertEquals(testBookingStatusCanChaneOnlyOwnerException.getMessage(), resultErrorResponse.getDescription());
    }

    @Test
    void catchCouldNotModifiedItem() {
        ItemCouldntBeModified testItemCouldntBeModifiedException = new ItemCouldntBeModified("Booking Status Can Chane Only Owner ");
        ErrorResponse resultErrorResponse = errorHandler.catchCouldNotModifiedItem(testItemCouldntBeModifiedException);
        assertNotNull(resultErrorResponse);
        Assertions.assertEquals(testItemCouldntBeModifiedException.getMessage(), resultErrorResponse.getDescription());
    }

    @Test
    void catchNonUniqueEmailCreating() {
        NonUniqueEmail testNonUniqueEmailException = new NonUniqueEmail("Booking Status Can Chane Only Owner ");
        ErrorResponse resultErrorResponse = errorHandler.catchNonUniqueEmailCreating(testNonUniqueEmailException);
        assertNotNull(resultErrorResponse);
        Assertions.assertEquals(testNonUniqueEmailException.getMessage(), resultErrorResponse.getDescription());

    }
}