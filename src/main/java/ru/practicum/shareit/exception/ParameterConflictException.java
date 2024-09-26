package ru.practicum.shareit.exception;

import lombok.Getter;

@Getter
public class ParameterConflictException extends RuntimeException {

    private final String parameter;
    private final String reason;

    public ParameterConflictException(String parameter, String reason) {
        this.parameter = parameter;
        this.reason = reason;
    }
}
