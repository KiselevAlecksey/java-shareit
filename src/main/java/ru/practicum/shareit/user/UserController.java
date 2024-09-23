package ru.practicum.shareit.user;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.UserDtoResponse;
import ru.practicum.shareit.util.Marker;
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
    public Collection<UserDtoResponse> findAll() {
        return userService.findAll();
    }

    @GetMapping("/{id}")
    public UserDtoResponse getById(@PathVariable long id) {
        log.info("User get by id {} start", id);
        return userService.getById(id);
    }

    @PostMapping
    @Validated({Marker.OnCreate.class})
    @ResponseStatus(HttpStatus.CREATED)
    public UserDtoResponse create(@RequestBody @Valid UserDto userRequest) {
        UserDtoResponse created = userService.create(userRequest);
        log.info("Created user is {}", userRequest.getEmail());
        return created;
    }

    @Validated({Marker.OnUpdate.class})
    @PatchMapping("/{id}")
    public UserDtoResponse update(@RequestBody @Valid UserDto userRequest, @PathVariable long id) {
        UserDtoResponse updated = userService.update(userRequest, id);
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
