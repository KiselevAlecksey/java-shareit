package ru.practicum.shareit.request.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Null;
import lombok.*;
import lombok.experimental.FieldDefaults;
import ru.practicum.shareit.util.Marker;
import ru.practicum.shareit.validator.ValidString;

/**
 * TODO Sprint add-item-requests.
 */
@Getter
@Setter
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ItemRequestDto {

    @Null(groups = {Marker.OnCreate.class})
    Long id;

    @ValidString(groups = {Marker.OnUpdate.class})
    @NotBlank(groups = {Marker.OnCreate.class})
    String name;

    @ValidString(groups = {Marker.OnUpdate.class})
    @NotBlank(groups = {Marker.OnCreate.class})
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
