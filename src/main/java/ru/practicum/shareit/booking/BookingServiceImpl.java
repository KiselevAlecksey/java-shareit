package ru.practicum.shareit.booking;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.dto.BookingApproveDto;
import ru.practicum.shareit.booking.dto.BookingCreateDto;
import ru.practicum.shareit.booking.dto.BookingResponseDto;
import ru.practicum.shareit.booking.dto.BookingUpdateDto;
import ru.practicum.shareit.booking.mapper.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.exception.ConditionsNotMetException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.ParameterNotValidException;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.List;

import static ru.practicum.shareit.util.Const.ONE_DAY_IN_MINUTES;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BookingServiceImpl implements BookingService {
    final BookingRepository bookingRepository;

    final ItemRepository itemRepository;

    final UserRepository userRepository;

    final BookingMapper bookingMapper;

    @Override
    public BookingResponseDto create(BookingCreateDto bookingRequest) {

        User user = checkUserExist(bookingRequest.getConsumerId());

        Item item = itemRepository.findById(bookingRequest.getItemId())
                .orElseThrow(() -> new NotFoundException("Предмет не найден"));

        if (!item.getAvailable()) {

            if (item.getLastBooking() != null && item.getLastBooking().isBefore(LocalDateTime.now())) {
                item.setAvailable(true);
            } else {
                throw new ConditionsNotMetException("Предмет недоступен для брони");
            }
        }

        bookingRequest.setItem(item);

        Booking booking = bookingMapper.mapToBooking(bookingRequest);

        booking.setConsumer(user);

        booking.setOwner(item.getOwner());

        booking.setConfirmTime(booking.getStartBooking().plusMinutes(ONE_DAY_IN_MINUTES));

        Booking bookingCreated = bookingRepository.save(booking);

        return bookingMapper.mapToBookingResponse(bookingCreated);
    }

    @Override
    public BookingResponseDto getById(long userId, long bookingId) {

        User user = checkUserExist(userId);

        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new NotFoundException("Бронь не найдена"));

        return bookingMapper.mapToBookingResponse(booking);
    }

    @Override
    public BookingResponseDto update(BookingUpdateDto bookingRequest) {

        Booking booking = bookingRepository.findById(bookingRequest.getId())
                .orElseThrow(() -> new NotFoundException("Бронь не найдена"));

        if (!booking.getConsumer().getId().equals(bookingRequest.getConsumer().getId())) {
            throw new NotFoundException("Обновить бронь может только создатель брони");
        }

        bookingMapper.updateBookingFields(booking, bookingRequest);

        Booking bookingUpdated = bookingRepository.save(booking);

        updateItemRepository(booking);

        return bookingMapper.mapToBookingResponse(bookingUpdated);
    }

    @Override
    public BookingResponseDto approve(BookingApproveDto bookingRequest) {

        Booking booking = bookingRepository.findById(bookingRequest.getId())
                .orElseThrow(() -> new NotFoundException("Бронь не найдена"));

        if (!booking.getOwner().getId().equals(bookingRequest.getOwner().getId())) {
            throw new ParameterNotValidException("Подтвердить заявку может только владелец вещи",
                    booking.getConsumer().getName());
        }

        if (booking.getConfirmTime().isAfter(booking.getStartBooking().plusMinutes(ONE_DAY_IN_MINUTES))) {
            throw new NotFoundException("Время подтверждения заявки истекло");
        }

        booking.setAvailable(false);

        if (bookingRequest.getApproved()) {
            booking.setStatus(Status.APPROVED);
        } else {
            booking.setStatus(Status.REJECTED);
        }

        Booking bookingUpdated = bookingRepository.save(booking);

        updateItemRepository(booking);

        return bookingMapper.mapToBookingResponse(bookingUpdated);
    }

    @Override
    public void delete(long id) {

        Booking booking = bookingRepository.findById(id).orElseThrow(
                () -> new NotFoundException("Бронь не найдена")
        );

        if (!booking.getConsumer().getId().equals(id)) {
            throw new NotFoundException("Удалить бронь может только создатель брони");
        }

        Item item = itemRepository.findById(booking.getItem().getId()).orElseThrow(
                () -> new NotFoundException("Предмет не найден")
        );

        bookingRepository.deleteById(id);
    }

    @Override
    public List<BookingResponseDto> getAllBookingsByConsumerId(long id, String state) {

        checkUserExist(id);

        Status status;

        List<BookingResponseDto> list;

        try {
            status = Status.valueOf(state);
        } catch (IllegalArgumentException e) {
            throw new ParameterNotValidException("Неверное значение", state);
        }

        list = switch (status) {
            case Status.ALL -> bookingRepository.findAllByConsumerId(id).stream()
                    .map(bookingMapper::mapToBookingResponse)
                    .toList();
            case Status.CURRENT -> bookingRepository.findCurrentByConsumerId(id).stream()
                    .map(bookingMapper::mapToBookingResponse)
                    .toList();
            case Status.PAST -> bookingRepository.findPastByConsumerId(id).stream()
                    .map(bookingMapper::mapToBookingResponse)
                    .toList();
            case Status.FUTURE -> bookingRepository.findFutureByConsumerId(id).stream()
                    .map(bookingMapper::mapToBookingResponse)
                    .toList();
            case Status.WAITING -> bookingRepository.findWaitingByConsumerId(id).stream()
                    .map(bookingMapper::mapToBookingResponse)
                    .toList();
            case Status.REJECTED -> bookingRepository.findRejectedByConsumerId(id).stream()
                    .map(bookingMapper::mapToBookingResponse)
                    .toList();
            default -> throw new ParameterNotValidException("Не поддерживаемое состояние:", state);
        };

        return list;
    }

    @Override
    public List<BookingResponseDto> getAllBookingsByOwnerId(long id, String state) {
        User user = checkUserExist(id);

        Integer count = itemRepository.findCountBookingsByUserId(user.getId());

        if (count < 1) {
            throw new NotFoundException("У пользователя отсутствуют предметы");
        }

        Status status;

        List<BookingResponseDto> list;

        try {
            status = Status.valueOf(state);
        } catch (IllegalArgumentException e) {
            throw new ParameterNotValidException("Неверное значение", state);
        }

        list = switch (status) {
            case Status.ALL -> bookingRepository.findAllByOwnerId(id).stream()
                    .map(bookingMapper::mapToBookingResponse)
                    .toList();
            case Status.CURRENT -> bookingRepository.findCurrentByOwnerId(id).stream()
                    .map(bookingMapper::mapToBookingResponse)
                    .toList();
            case Status.PAST -> bookingRepository.findPastByOwnerId(id).stream()
                    .map(bookingMapper::mapToBookingResponse)
                    .toList();
            case Status.FUTURE -> bookingRepository.findFutureByOwnerId(id).stream()
                    .map(bookingMapper::mapToBookingResponse)
                    .toList();
            case Status.WAITING -> bookingRepository.findWaitingByOwnerId(id).stream()
                    .map(bookingMapper::mapToBookingResponse)
                    .toList();
            case Status.REJECTED -> bookingRepository.findRejectedByOwnerId(id).stream()
                    .map(bookingMapper::mapToBookingResponse)
                    .toList();
            default -> throw new ParameterNotValidException("Не поддерживаемое состояние:", state);
        };

        return list;
    }

    private void updateItemRepository(Booking booking) {
        Item item = itemRepository.findById(booking.getItem().getId())
                .orElseThrow(() -> new NotFoundException("Предмет не найден"));
        item.setAvailable(booking.getAvailable());
        itemRepository.save(item);
    }

    private User checkUserExist(long id) {
        return userRepository.findById(id).orElseThrow(() -> new NotFoundException("Пользователь не найден"));
    }
}
