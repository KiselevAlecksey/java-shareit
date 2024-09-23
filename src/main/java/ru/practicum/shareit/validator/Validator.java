package ru.practicum.shareit.validator;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.exception.ConditionsNotMetException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.ParameterNotValidException;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.review.dto.ReviewDto;
import ru.practicum.shareit.user.UserRepository;

import ru.practicum.shareit.user.dto.UserDto;

@Component
@RequiredArgsConstructor
public class Validator {

    private final UserRepository userRepository;

    private final ItemRepository itemRepository;

    public void validateUserRequest(UserDto userRequest) {
        StringBuilder errorMessage = new StringBuilder();

        if (isEmailInvalid(userRequest.getEmail())) {
            errorMessage.append("Поле почта не может быть пустым или пропущен знак @.\n");
        }

        if (!errorMessage.isEmpty()) {
            throw new ParameterNotValidException("email", errorMessage.toString().trim());
        }
    }

    private boolean isEmailInvalid(String email) {
        if (email == null) return false;
        return email.isBlank() || email.indexOf('@') == -1;
    }

    public void validateBookingRequest(BookingDto bookingRequest) {
        StringBuilder errorMessage = new StringBuilder();

        if (isOwnerInvalid(bookingRequest)) {
            errorMessage.append("Неверный id владельца, ");
        }

        if (!errorMessage.isEmpty()) {
            throw new ParameterNotValidException("owner", errorMessage.toString().trim());
        }
    }

    private boolean isOwnerInvalid(BookingDto bookingRequest) {
        Long id = bookingRequest.getOwnerId();

        if (id == null) {
            throw new ConditionsNotMetException("Неверный id пользователя");
        }

        if (id < 1) {
            throw new NotFoundException("Пользователь не найден");
        }

        return userRepository.getById(id).isEmpty();
    }

    public void validateReviewRequest(ReviewDto reviewRequest) {
        StringBuilder errorMessage = new StringBuilder();

        if (isConsumerInvalid(reviewRequest)) {
            errorMessage.append("Неверный id пользователя, ");
        }

        if (isOwnerInvalid(reviewRequest)) {
            errorMessage.append("Неверный id владельца, ");
        }

        if (isItemInvalid(reviewRequest)) {
            errorMessage.append("Неверный id предмета, ");
        }

        if (!errorMessage.isEmpty()) {
            throw new NotFoundException(errorMessage.toString().trim());
        }
    }

    private boolean isConsumerInvalid(ReviewDto reviewRequest) {
        Long id = reviewRequest.getConsumerId();

        if (id == null) {
            throw new ConditionsNotMetException("Неверный id пользователя");
        }

        if (id < 1) {
            throw new NotFoundException("Пользователь не найден");
        }

        return userRepository.getById(id).isEmpty();
    }

    private boolean isOwnerInvalid(ReviewDto reviewRequest) {
        Long id = reviewRequest.getOwnerId();

        if (id == null) {
            throw new ConditionsNotMetException("Неверный id пользователя");
        }

        if (id < 1) {
            throw new NotFoundException("Пользователь не найден");
        }

        return userRepository.getById(id).isEmpty();
    }

    private boolean isItemInvalid(ReviewDto reviewRequest) {
        Long id = reviewRequest.getItemId();

        if (id == null) {
            throw new ConditionsNotMetException("Неверный id пользователя");
        }

        if (id < 1) {
            throw new NotFoundException("Пользователь не найден");
        }

        return itemRepository.get(reviewRequest.getOwnerId(), id).isEmpty();
    }

}
