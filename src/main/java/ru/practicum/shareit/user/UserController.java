package ru.practicum.shareit.user;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.NewUserRequest;
import ru.practicum.shareit.user.dto.UpdateUserRequest;
import ru.practicum.shareit.user.dto.UserDto;

import java.util.Collection;

/**
 * TODO Sprint add-controllers.
 */
@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/users")
public class UserController {
    private final UserService userService;

    @GetMapping
    public Collection<UserDto> findAll() {
        return userService.findAll();
    }

    @GetMapping("/{id}")
    public UserDto getById(@PathVariable long id) {
        log.info("User get by id {} start", id);
        return userService.getById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserDto create(@RequestBody @Valid NewUserRequest userRequest) {
        UserDto created = userService.create(userRequest);
        log.info("Created user is {}", userRequest.getEmail());
        return created;
    }

    @PatchMapping("/{id}")
    public UserDto update(@RequestBody @Valid UpdateUserRequest userRequest, @PathVariable long id) {
        UserDto updated = userService.update(userRequest, id);
        log.info("Updated user is id {} complete", id);
        return updated;
    }

    @DeleteMapping("/{id}")
    public boolean userRemove(@PathVariable long id) {
        boolean response = userService.remove(id);
        log.info("Users remove user id {},complete", id);
        return response;
    }
}
