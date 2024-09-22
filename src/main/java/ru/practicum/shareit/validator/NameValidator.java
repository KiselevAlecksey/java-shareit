package ru.practicum.shareit.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class NameValidator implements ConstraintValidator<ValidName, String> {

    @Override
    public boolean isValid(String name, ConstraintValidatorContext context) {
        if (name == null) {
            return true;
        }
        return !name.trim().isEmpty();
    }
}
