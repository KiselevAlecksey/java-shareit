package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.ParameterConflictException;
import ru.practicum.shareit.exception.ParameterNotValidException;
import ru.practicum.shareit.user.dto.UserCreateDto;
import ru.practicum.shareit.user.dto.UserUpdateDto;
import ru.practicum.shareit.user.dto.UserDtoResponse;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    private final UserMapper userMapper;

    @Override
    public List<UserDtoResponse> findAll() {
        return userRepository.findAll().stream().map(userMapper::mapToUserDto).toList();
    }

    @Override
    public UserDtoResponse getById(long id) {
        User user = userRepository.getById(id)
                .orElseThrow(() -> new NotFoundException("Пользователь не найден"));

        return userMapper.mapToUserDto(user);
    }

    @Override
    public UserDtoResponse create(UserCreateDto userRequest) {

        if (userRequest.getEmail() == null) {
            throw new ParameterNotValidException("email", "Поле email является обязательным");
        }

        User user = userMapper.mapToUser(userRequest);

        checkEmailConflict(user.getEmail());

        User userCreated = userRepository.create(user);

        return userMapper.mapToUserDto(userCreated);
    }

    @Override
    public UserDtoResponse update(UserUpdateDto userRequest) {

        if (userRepository.getById(userRequest.getId()).isPresent()) {

            User userUpdated = userRepository.getById(userRequest.getId())
                    .orElseThrow(() -> new NotFoundException("Пользователь не найден"));

            if (!(userUpdated.getEmail().equals(userRequest.getEmail()))) {
                checkEmailConflict(userMapper.mapToUser(userRequest).getEmail());
            }

            userMapper.updateUserFields(userUpdated, userRequest);

            User user = userRepository.update(userUpdated);

            return userMapper.mapToUserDto(user);
        }
        throw new NotFoundException("Пользователь с id = " + userRequest.getId() + " не найден");
    }

    @Override
    public boolean remove(long id) {

        if (userRepository.getById(id).isEmpty()) {
            return false;
        }

        return userRepository.remove(id);
    }

    private void checkEmailConflict(String email) {
        List<String> emails = userRepository.getEmails();

        if (emails.contains(email)) {
            throw new ParameterConflictException("email", "Тайкой email уже занят");
        }
    }
}
