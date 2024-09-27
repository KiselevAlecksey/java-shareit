package ru.practicum.shareit.item.model;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import ru.practicum.shareit.user.model.User;

@Data
@Builder(toBuilder = true)
@EqualsAndHashCode(of = "id")
public class Item {

    Long id;

    User owner;

    String name;

    String description;

    Boolean available;

    String category;
}
