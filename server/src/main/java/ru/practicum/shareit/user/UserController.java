package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.UserCreateDto;
import ru.practicum.shareit.user.dto.UserUpdateDto;
import ru.practicum.shareit.user.dto.UserResponseDto;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/users")
public class UserController {
    private final UserService userService;

    @GetMapping
    public List<UserResponseDto> findAll() {
        List<UserResponseDto> list = userService.findAll();
        return list;
    }

    @GetMapping("/{id}")
    public UserResponseDto getById(@PathVariable long id) {
        UserResponseDto item = userService.getById(id);
        return item;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserResponseDto create(@RequestBody UserCreateDto userRequest) {
        UserResponseDto created = userService.create(userRequest);
        return created;
    }

    @PatchMapping("/{id}")
    public UserResponseDto update(@RequestBody UserUpdateDto userRequest) {
        UserResponseDto updated = userService.update(userRequest);
        return updated;
    }

    @DeleteMapping("/{id}")
    public void userRemove(@PathVariable long id) {
        userService.remove(id);
    }
}
