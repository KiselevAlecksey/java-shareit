package ru.practicum.shareit.request.model;

import lombok.Builder;
import lombok.Data;

/**
 * TODO Sprint add-item-requests.
 */
@Data
@Builder(toBuilder = true)
public class ItemRequest {

    Long id;

    Long userId;

    String name;

    String description;

    Boolean available;

    String category;
}
