package ru.practicum.shareit.request.dto;

import lombok.Data;

@Data
public class UpdateItemRequestDto {

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
