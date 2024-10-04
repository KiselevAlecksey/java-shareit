package ru.practicum.shareit.user;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.UserCreateDto;
import ru.practicum.shareit.user.dto.UserUpdateDto;
import ru.practicum.shareit.user.dto.UserResponseDto;

import java.util.List;

@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/users")
public class UserController {
    private final UserService userService;

    @GetMapping
    public List<UserResponseDto> findAll() {
        log.info("==> Users get all start");
        List<UserResponseDto> list = userService.findAll();
        log.info("<== Users get all complete");
        return list;
    }

    @GetMapping("/{id}")
    public UserResponseDto getById(@PathVariable long id) {
        log.info("==> User get {} start", id);
        UserResponseDto item = userService.getById(id);
        log.info("<== User get {} complete", id);
        return item;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserResponseDto create(@RequestBody @Valid UserCreateDto userRequest) {
        log.info("==> Create user is {} start", userRequest.getEmail());
        UserResponseDto created = userService.create(userRequest);
        log.info("<== Created user is {} complete", userRequest.getEmail());
        return created;
    }

    @PatchMapping("/{id}")
    public UserResponseDto update(@RequestBody @Valid UserUpdateDto userRequest, @PathVariable long id) {
        log.info("==> Update user is id {} start", id);
        userRequest.setId(id);
        UserResponseDto updated = userService.update(userRequest);
        log.info("<== Updated user is id {} complete", id);
        return updated;
    }

    @DeleteMapping("/{id}")
    public void userRemove(@PathVariable long id) {
        log.info("==> Users remove user id {} start", id);
        userService.remove(id);
        log.info("<== Users remove user id {} complete", id);
    }
}
