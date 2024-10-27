package ru.practicum.shareit.exception;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import ru.practicum.shareit.exception.model.ErrorResponse;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName("ErrorHandler")
class ErrorHandlerTest {

    @InjectMocks
    private ErrorHandler errorHandler;

    public ErrorHandlerTest() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("Должен обрабатывать ошибки 'Not Found'")
    void should_handle_not_found_error() {
        String expectedMessage = "404 NOT_FOUND";
        NotFoundException exception = new NotFoundException(expectedMessage);

        ErrorResponse response = errorHandler.handleNotFound(exception);

        assertEquals("404 NOT_FOUND", response.getError());
    }

    @Test
    @DisplayName("Должен обрабатывать конфликты параметров")
    void should_handle_parameter_conflict() {
        String parameter = "id";
        String reason = "id must be positive";
        ParameterConflictException exception = new ParameterConflictException(parameter, reason);

        ErrorResponse response = errorHandler.handleParameterConflict(exception);

        assertEquals("Некорректное значение параметра id: id must be positive", response.getError());
    }

    @Test
    @DisplayName("Должен обрабатывать ошибки 'Bad Request'")
    void should_handle_bad_request_error_conditions_not_met() {
        ErrorResponse response = errorHandler
                .handleConditionsNotMetException(new ConditionsNotMetException("Параметр должен быть задан"));

        assertEquals("Условия не соблюдены: Параметр должен быть задан", response.getError());
    }

    @Test
    @DisplayName("Должен обрабатывать ошибки 'Bad Request' для некорректных параметров")
    void should_handle_bad_request_error_parameter_not_valid() {
        String parameter = "age";
        String reason = "Age must be a positive number";

        ErrorResponse response = errorHandler
                .handleParameterNotValid(new ParameterNotValidException(parameter, reason));

        assertEquals("Некорректное значение параметра age: Age must be a positive number", response.getError());
    }

    @Test
    @DisplayName("Должен обрабатывать общие ошибки 'Bad Request'")
    void should_handle_generic_bad_request_error() {
        Throwable exception = new Throwable("Произошла непредвиденная ошибка");

        ErrorResponse response = errorHandler.handle(exception);

        assertEquals("Произошла непредвиденная ошибка", response.getError());
    }
}