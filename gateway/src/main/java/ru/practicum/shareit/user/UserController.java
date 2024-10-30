package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.UserCreateDto;
import ru.practicum.shareit.user.dto.UserUpdateDto;

@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/users")
public class UserController {
    private final UserClient userClient;

    @GetMapping
    public ResponseEntity<Object> findAll() {
        log.info("==> Users get all start");
        ResponseEntity<Object> list = userClient.findAll();
        log.info("<== Users get all complete");
        return list;
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getById(@PathVariable long id) {
        log.info("==> User get {} start", id);
        ResponseEntity<Object> user = userClient.getById(id);
        log.info("<== User get {} complete", id);
        return user;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Object> create(@RequestBody @Validated UserCreateDto userRequest) {
        log.info("==> Create user is {} start", userRequest.getEmail());
        ResponseEntity<Object> created = userClient.create(userRequest);
        log.info("<== Created user is {} complete", userRequest.getEmail());
        return created;
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Object> update(@RequestBody @Validated UserUpdateDto userRequest, @PathVariable long id) {
        log.info("==> Update user is id {} start", id);
        userRequest.setId(id);
        ResponseEntity<Object> updated = userClient.update(userRequest);
        log.info("<== Updated user is id {} complete", id);
        return updated;
    }

    @DeleteMapping("/{id}")
    public void userRemove(@PathVariable long id) {
        log.info("==> Users remove user id {} start", id);
        userClient.delete(id);
        log.info("<== Users remove user id {} complete", id);
    }
}
