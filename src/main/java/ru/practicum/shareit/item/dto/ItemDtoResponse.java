package ru.practicum.shareit.item.dto;

import lombok.Data;

/**
 * TODO Sprint add-controllers.
 */

@Data
public class ItemDtoResponse {

    Long id;

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
