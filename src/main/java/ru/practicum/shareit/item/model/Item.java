package ru.practicum.shareit.item.model;

import lombok.Builder;
import lombok.Data;

/**
 * TODO Sprint add-controllers.
 */

@Data
@Builder(toBuilder = true)
public class Item {

    Long id;

    Long ownerId;

    String name;

    String description;

    Boolean available;

    String category;
}
