package ru.practicum.shareit.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class EmailValidator implements ConstraintValidator<ValidEmail, String> {

    @Override
    public boolean isValid(String email, ConstraintValidatorContext context) {
        if (email == null) {
            return true; // Не проверяем, если null
        }
        return email.matches("^[A-Za-z0-9+_.-]+@(.+)$"); // Проверка на формат email
    }
}
