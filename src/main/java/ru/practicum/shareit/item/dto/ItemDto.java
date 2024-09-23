package ru.practicum.shareit.item.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;
import lombok.*;
import lombok.experimental.FieldDefaults;
import ru.practicum.shareit.util.Marker;
import ru.practicum.shareit.validator.ValidString;

/**
 * TODO Sprint add-controllers.
 */

@Getter
@Setter
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ItemDto {

    @Null(groups = {Marker.OnCreate.class})
    Long id;

    @ValidString(groups = {Marker.OnUpdate.class})
    @NotBlank(groups = {Marker.OnCreate.class})
    String name;

    @ValidString(groups = {Marker.OnUpdate.class})
    @NotBlank(groups = {Marker.OnCreate.class})
    String description;

    @NotNull(groups = {Marker.OnCreate.class})
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
