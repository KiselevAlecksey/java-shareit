package ru.practicum.shareit.request.dto;

import lombok.Data;
import ru.practicum.shareit.validator.ValidName;

@Data
public class UpdateItemRequestDto {

    @ValidName
    String name;

    String description;

    public boolean hasName() {
        return isNotBlank(name);
    }

    public boolean hasDescription() {
        return isNotBlank(description);
    }

    boolean isNotBlank(String value) {
        return value != null && !value.isBlank();
    }
}
