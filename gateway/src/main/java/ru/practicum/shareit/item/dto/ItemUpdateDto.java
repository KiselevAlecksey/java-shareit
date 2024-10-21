package ru.practicum.shareit.item.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.Null;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ItemUpdateDto {

    @Null
    Long id;

    @Size(min = 1)
    String name;

    @Size(min = 1)
    String description;

    Boolean available;

    Long ownerId;
}
