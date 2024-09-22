package ru.practicum.shareit.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class NewUserRequest implements UserRequest {

    String name;

    @NotBlank(message = "Email является обязательным")
    @Email(message = "Некорректный формат email")
    String email;
}