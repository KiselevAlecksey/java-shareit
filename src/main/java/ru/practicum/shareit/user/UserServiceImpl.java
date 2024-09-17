package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.ParameterConflictException;
import ru.practicum.shareit.exception.ParameterNotValidException;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.validator.Validator;

import java.util.Collection;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    private final Validator validator;

    @Override
    public Collection<UserDto> findAll() {
        return userRepository.findAll().stream().map(UserMapper::mapToUserDto).toList();
    }

    @Override
    public UserDto getById(long id) {
        User user = userRepository.getById(id).orElseThrow(
                () -> new NotFoundException("Пользователь не найден")
        );

        return UserMapper.mapToUserDto(user);
    }

    @Override
    public UserDto create(UserDto userRequest) {

        if (userRequest.getEmail() == null) {
            throw new ParameterNotValidException("email", "Поле email является обязательным");
        }

        User user = UserMapper.mapToUser(userRequest);

        validateEmailConflict(user);

        validator.validateUserRequest(userRequest);

        User userCreated = userRepository.create(user).orElseThrow(
                () -> new NotFoundException("Пользователь не найден")
        );

        return UserMapper.mapToUserDto(userCreated);
    }

    @Override
    public UserDto update(UserDto userRequest, Long id) {

        userRequest.setId(id);

        if (userRequest.getId() == null) {
            throw new NotFoundException("Id должен быть указан");
        }

        if (userRepository.getById(id).isPresent()) {

            validator.validateUserRequest(userRequest);

            User userUpdated = userRepository.getById(userRequest.getId())
                    .orElseThrow(() -> new NotFoundException("Пользователь не найден")
                    );

            if (!(userUpdated.getEmail().equals(userRequest.getEmail()))) {
                validateEmailConflict(UserMapper.mapToUser(userRequest));
            }

            UserMapper.updateUserFields(userUpdated, userRequest);

            User user = userRepository.update(userUpdated)
                    .orElseThrow(() -> new NotFoundException("Пользователь не найден")
                    );

            return UserMapper.mapToUserDto(user);
        }
        throw new NotFoundException("Пользователь с id = " + userRequest.getId() + " не найден");
    }

    @Override
    public boolean remove(long id) {
        return userRepository.remove(id);
    }

    private void validateEmailConflict(User user) {
        List<User> users = userRepository.findAll().stream()
                .filter(user1 -> user1.getEmail().equals(user.getEmail()))
                .toList();

        if (!users.isEmpty()) {
            throw new ParameterConflictException("email", "Тайкой email уже занят");
        }
    }
}
