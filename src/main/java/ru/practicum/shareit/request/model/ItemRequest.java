package ru.practicum.shareit.request.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

/**
 * TODO Sprint add-item-requests.
 */
@Data
@Builder(toBuilder = true)
public class ItemRequest {

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
