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
        log.info("Получен статус 404 Not found");

        return new ErrorResponse(e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handle(final Throwable e) {
        log.trace("Получен статус 500 Internal server error ", e);

        return new ErrorResponse("Произошла непредвиденная ошибка ", e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse handleParameterConflict(final ParameterConflictException e) {
        log.info("Received status 409 Conflict");

        return new ErrorResponse("Некорректное значение параметра "
                + e.getParameter() + ": " + e.getReason());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleParameterNotValid(final MethodArgumentNotValidException e) {
        log.info("Получен статус 400 Bad request");

        String message = null;

        FieldError fieldError = e.getBindingResult().getFieldError();

        if (fieldError != null) {
            message = fieldError.getDefaultMessage();
        }

        return new ErrorResponse("Некорректное значение параметра "
                + e.getParameter().getParameterName() + ": ", message);
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleBadRequest(final RuntimeException e) {
        log.info("Получен статус 400 Bad request");

        String message;

        switch (e) {
            case ConditionsNotMetException conditionsNotMetException ->
                    message = "Условия не соблюдены: " + e.getMessage();
            case ParameterNotValidException paramEx ->
                    message = "Некорректное значение параметра " + paramEx.getParameter() + ": " + paramEx.getReason();
            case ConstraintViolationException constraintViolationException ->
                    message = "Некорректное значение параметра: " + e.getMessage();
            case null, default -> message = "Некорректный запрос";
        }

        return new ErrorResponse(message);
    }
}
