package ru.practicum.shareit.item.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ItemUpdateDto {

    Long id;

    String name;

    String description;

    Boolean available;

    Long ownerId;

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
