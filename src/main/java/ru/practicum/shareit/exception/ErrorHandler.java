package ru.practicum.shareit.exception;

import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.practicum.shareit.exception.model.ErrorResponse;

@Slf4j
@RestControllerAdvice
public class ErrorHandler {

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleNotFound(final NotFoundException e) {
        log.trace("Получен статус 404 Not found {}", e.getMessage(), e);

        return new ErrorResponse(
                e.getMessage()
        );
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleConditionsNotMet(final ConditionsNotMetException e) {
        log.trace("Получен статус 400 Bad request {}", e.getMessage(), e);

        return new ErrorResponse(
                e.getMessage(),
                "Условия не соблюдены"
        );
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handle(final Throwable e) {
        log.trace("Получен статус 500 Internal server error {}", e.getMessage(), e);

        return new ErrorResponse(
                "Произошла непредвиденная ошибка"
        );
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleParameterNotValid(final ParameterNotValidException e) {
        log.trace("Получен статус 400 Bad request {}", e.getMessage(), e);

        return new ErrorResponse(
                "Некорректное значение параметра " + e.getParameter() + ": " + e.getReason()
        );
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse handleParameterConflict(final ParameterConflictException e) {
        log.error("Received status 409 Conflict: {}", e.getMessage(), e);

        return new ErrorResponse(
                "Некорректное значение параметра " + e.getParameter() + ": " + e.getReason()
        );
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleConstraintViolationException(ConstraintViolationException e) {
        log.trace("Получен статус 400 Bad request {}", e.getMessage(), e);

        return new ErrorResponse("Некорректное значение параметра ", e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleParameterNotValid(final MethodArgumentNotValidException e) {
        log.trace("Получен статус 400 Bad request {}", e.getMessage(), e);
        String message = null;
        FieldError fieldError = e.getBindingResult().getFieldError();
        if (fieldError != null) {
            message = fieldError.getDefaultMessage();
        }
        return new ErrorResponse(
                "Некорректное значение параметра " + e.getParameter().getParameterName() + ": ", message
        );
    }
}
