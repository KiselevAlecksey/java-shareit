package ru.practicum.shareit.user.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;
import lombok.*;
import lombok.experimental.FieldDefaults;
import ru.practicum.shareit.util.Marker;
import ru.practicum.shareit.validator.ValidString;

@Getter
@Setter
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserDto {

    @Null(groups = {Marker.OnCreate.class})
    Long id;

    @ValidString(groups = {Marker.OnUpdate.class})
    @NotBlank(groups = {Marker.OnCreate.class})
    String name;

    @Email(groups = {Marker.OnCreate.class, Marker.OnUpdate.class})
    @NotNull(groups = {Marker.OnCreate.class})
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
