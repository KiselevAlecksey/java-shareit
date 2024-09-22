package ru.practicum.shareit.item.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class NewItemRequest {

    @NotBlank
    String name;

    String description;

    @NotNull
    Boolean available;
}
