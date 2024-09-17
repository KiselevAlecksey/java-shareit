package ru.practicum.shareit.item.dto;

/**
 * TODO Sprint add-controllers.
 */

public record ItemDtoResponse(
        Long id,

        String name,

        String description,

        Boolean available) {
}
