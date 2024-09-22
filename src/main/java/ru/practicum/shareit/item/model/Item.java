package ru.practicum.shareit.item.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

/**
 * TODO Sprint add-controllers.
 */

@Data
@Builder(toBuilder = true)
public class Item {

    Long id;

    @NotBlank
    Long ownerId;

    @NotBlank
    String name;

    @NotBlank
    String description;

    @NotNull
    Boolean available;

    String category;
}