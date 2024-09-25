package ru.practicum.shareit.user.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Null;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UpdateUserDtoRequest {

    @Null
    Long id;

    @Size(min = 1)
    String name;

    @Email
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
