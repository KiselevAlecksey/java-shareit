package ru.practicum.shareit.user.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import ru.practicum.shareit.validator.ValidEmail;
import ru.practicum.shareit.validator.ValidName;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UpdateUserRequest implements UserRequest {

    @ValidName
    String name;

    @ValidEmail
    String email;

    public boolean hasName() {
        return isNotBlank(name);
    }

    public boolean hasEmail() {
        return isNotBlank(email);
    }

    boolean isNotBlank(String value) {
        return value != null && !value.isBlank();
    }
}
