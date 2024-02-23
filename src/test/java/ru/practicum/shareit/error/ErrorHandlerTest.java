package ru.practicum.shareit.error;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import ru.practicum.shareit.exception.BookingException;
import ru.practicum.shareit.exception.BookingNotFoundException;
import ru.practicum.shareit.exception.EntityNotFoundException;
import ru.practicum.shareit.exception.ValidationException;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ErrorHandlerTest {

    @Autowired
    ErrorHandler errorHandler;
    String error = "textException";

    @Test
    void handlerEntityNotFoundException() {
        ErrorResponse errorResponse = errorHandler.handlerEntityNotFoundException(new EntityNotFoundException(error));
        assertEquals(errorResponse.getError(), error);
    }

    @Test
    void handlerHttpMediaTypeNotAcceptableException() {
        ErrorResponse errorResponse = errorHandler.handlerHttpMediaTypeNotAcceptableException(new HttpMediaTypeNotAcceptableException(error));
        assertEquals(errorResponse.getError(), error);
    }

    @Test
    void handlerValidationException() {
        ErrorResponse errorResponse = errorHandler.handlerValidationException(new ValidationException(error));
        assertEquals(errorResponse.getError(), error);
    }

    @Test
    void handlerBookingException() {
        ErrorResponse errorResponse = errorHandler.handlerBookingException(new BookingException(error));
        assertEquals(errorResponse.getError(), error);
    }

    @Test
    void handlerBookingNotFoundException() {
        ErrorResponse errorResponse = errorHandler.handlerBookingNotFoundException(new BookingNotFoundException(error));
        assertEquals(errorResponse.getError(), error);
    }
}