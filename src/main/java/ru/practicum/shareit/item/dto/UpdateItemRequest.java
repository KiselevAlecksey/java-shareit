package ru.practicum.shareit.item.dto;

import lombok.Data;
import ru.practicum.shareit.validator.ValidName;

@Data
public class UpdateItemRequest {

    @ValidName
    String name;

    String description;

    Boolean available;

    public boolean hasName() {
        return isNotBlank(name);
    }

    public boolean hasDescription() {
        return isNotBlank(description);
    }

    public boolean hasAvailable() {
        return available != null;
    }

    boolean isNotBlank(String value) {
        return value != null && !value.isBlank();
    }
}
