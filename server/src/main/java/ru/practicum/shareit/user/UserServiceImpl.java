package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.ParameterConflictException;
import ru.practicum.shareit.user.dto.UserCreateDto;
import ru.practicum.shareit.user.dto.UserUpdateDto;
import ru.practicum.shareit.user.dto.UserResponseDto;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;

import java.util.List;

@Service
@Transactional(readOnly = true)
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
    @Transactional(isolation = Isolation.SERIALIZABLE)
    public UserResponseDto create(UserCreateDto userRequest) {

        User user = userMapper.mapToUser(userRequest);

        checkEmailConflict(user.getEmail());

        User userCreated = userRepository.save(user);

        return userMapper.mapToUserDto(userCreated);
    }

    @Override
    @Transactional(isolation = Isolation.SERIALIZABLE)
    public UserResponseDto update(UserUpdateDto userRequest) {

        User userUpdated = userRepository.findById(userRequest.getId())
                .orElseThrow(() -> new NotFoundException("Пользователь не найден"));
        if (!(userUpdated.getEmail().equals(userRequest.getEmail()))) {
            checkEmailConflict(userMapper.mapToUser(userRequest).getEmail());
        }

        userMapper.updateUserFields(userUpdated, userRequest);

        User user = userRepository.save(userUpdated);

        return userMapper.mapToUserDto(user);
    }

    @Override
    @Transactional
    public void remove(long id) {
        userRepository.deleteById(id);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    private void checkEmailConflict(String email) {
        UserEmail userEmails = userRepository.findByEmailContainingIgnoreCase(email);

        if (userEmails != null && userEmails.getEmail().equals(email)) {
            throw new ParameterConflictException("email", "Тайкой email уже занят");
        }
    }
}