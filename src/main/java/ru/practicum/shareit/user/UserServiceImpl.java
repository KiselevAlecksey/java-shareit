package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.ParameterConflictException;
import ru.practicum.shareit.user.dto.UserCreateDto;
import ru.practicum.shareit.user.dto.UserUpdateDto;
import ru.practicum.shareit.user.dto.UserResponseDto;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    private final UserMapper userMapper;

    @Override
    public List<UserResponseDto> findAll() {
        return userRepository.findAll().stream().map(userMapper::mapToUserDto).toList();
    }

    @Override
    public UserResponseDto getById(long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Пользователь не найден"));

        return userMapper.mapToUserDto(user);
    }

    @Override
    public UserResponseDto create(UserCreateDto userRequest) {

        User user = userMapper.mapToUser(userRequest);

        checkEmailConflict(user.getEmail());

        User userCreated = userRepository.save(user);

        return userMapper.mapToUserDto(userCreated);
    }

    @Override
    public UserResponseDto update(UserUpdateDto userRequest) {

        User userUpdated = userRepository.findById(userRequest.getId())
                .orElseThrow(() -> new NotFoundException("Пользователь не найден"));

        if (userUpdated != null) {

            if (!(userUpdated.getEmail().equals(userRequest.getEmail()))) {
                checkEmailConflict(userMapper.mapToUser(userRequest).getEmail());
            }

            userMapper.updateUserFields(userUpdated, userRequest);

            User user = userRepository.save(userUpdated);

            return userMapper.mapToUserDto(user);
        }
        throw new NotFoundException("Пользователь с id = " + userRequest.getId() + " не найден");
    }

    @Override
    public void remove(long id) {

        if (userRepository.findById(id).isEmpty()) {
            return;
        }

        userRepository.deleteById(id);
    }

    private void checkEmailConflict(String email) {
        List<UserEmail> userEmails = userRepository.findAllByEmailContainingIgnoreCase(email);

        if (!userEmails.isEmpty() && userEmails.getFirst().getEmail().equals(email)) {
            throw new ParameterConflictException("email", "Тайкой email уже занят");
        }
    }
}
