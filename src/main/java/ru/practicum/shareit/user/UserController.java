package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
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
        log.error("User get by id {} start", id);
        return userService.getById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserDto create(@RequestBody UserDto userRequest) {
        log.error("User create {} start", userRequest);
        UserDto created = userService.create(userRequest);
        log.error("Created user is {}", userRequest);
        return created;
    }

    @PatchMapping("/{id}")
    public UserDto update(@RequestBody UserDto userRequest, @PathVariable long id) {
        log.error("User update {}, id {} start", userRequest, id);
        UserDto updated = userService.update(userRequest, id);
        log.error("Updated user is {}, id {} complete", userRequest, id);
        return updated;
    }

    @DeleteMapping("/{id}")
    public boolean userRemove(@PathVariable long id) {
        log.error("Users remove user id {},start", id);
        boolean response = userService.remove(id);
        log.error("Users remove user id {},complete", id);
        return response;
    }
}
