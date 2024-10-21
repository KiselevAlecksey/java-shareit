package ru.practicum.shareit.booking;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.dto.BookingApproveDto;
import ru.practicum.shareit.booking.dto.BookingCreateDto;
import ru.practicum.shareit.booking.dto.BookingResponseDto;
import ru.practicum.shareit.booking.dto.BookingUpdateDto;
import ru.practicum.shareit.booking.mapper.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingOwnerId;
import ru.practicum.shareit.exception.ConditionsNotMetException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.ParameterNotValidException;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.model.User;

import java.util.Collections;
import java.util.List;

import static ru.practicum.shareit.booking.BookingStatus.*;
import static ru.practicum.shareit.util.Const.ONE_DAY_IN_MINUTES;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BookingServiceImpl implements BookingService {

    static final Sort SORT_BY_START_BOOKING_DESC = Sort.by(Sort.Direction.DESC, "startBooking");

    final BookingRepository bookingRepository;

    final ItemRepository itemRepository;

    final UserRepository userRepository;

    final BookingMapper bookingMapper;

    @Override
    @Transactional(isolation = Isolation.SERIALIZABLE)
    public BookingResponseDto create(BookingCreateDto bookingRequest) {

        long itemId = bookingRequest.getItemId();

        User user = getUserIfExist(bookingRequest.getBookerId());

        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new NotFoundException("Предмет не найден"));
        if (!item.getAvailable()) {
            throw new ConditionsNotMetException("Предмет недоступен для брони");
        }

        Booking booking = bookingMapper.mapToBooking(bookingRequest);

        booking.setAvailable(item.getAvailable());
        booking.setItem(item);
        booking.setBooker(user);
        booking.setOwner(item.getOwner());
        booking.setConfirmTime(booking.getStartBooking().plusMinutes(ONE_DAY_IN_MINUTES));

        Booking bookingCreated = bookingRepository.save(booking);

        return bookingMapper.mapToBookingResponse(bookingCreated);
    }

    @Override
    public BookingResponseDto getById(long bookingId, long userId) {

        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new NotFoundException("Бронь не найдена"));

        if (!(booking.getBooker().getId() == (userId) || booking.getOwner().getId() == (userId))) {
            throw new NotFoundException("Пользователь должен быть владельцем вещи или создателем брони");
        }
        return bookingMapper.mapToBookingResponse(booking);
    }

    @Override
    @Transactional(isolation = Isolation.SERIALIZABLE)
    public BookingResponseDto update(BookingUpdateDto bookingRequest) {

        Booking booking = bookingRepository.findById(bookingRequest.getId())
                .orElseThrow(() -> new NotFoundException("Бронь не найдена"));

        if (!booking.getBooker().getId().equals(bookingRequest.getBookerId())) {
            throw new NotFoundException("Обновить бронь может только создатель брони");
        }

        bookingMapper.updateBookingFields(booking, bookingRequest);

        Booking bookingUpdated = bookingRepository.save(booking);

        return bookingMapper.mapToBookingResponse(bookingUpdated);
    }

    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED)
    public BookingResponseDto approve(BookingApproveDto bookingRequest) {

        Booking booking = bookingRepository.findById(bookingRequest.getId())
                .orElseThrow(() -> new NotFoundException("Бронь не найдена"));

        if (!booking.getOwner().getId().equals(bookingRequest.getOwnerId())) {
            throw new ParameterNotValidException("Подтвердить заявку может только владелец вещи",
                    booking.getBooker().getName());
        }

        if (booking.getConfirmTime().isAfter(booking.getStartBooking().plusMinutes(ONE_DAY_IN_MINUTES))) {
            throw new NotFoundException("Время подтверждения заявки истекло");
        }

        booking.setAvailable(false);

        if (bookingRequest.getApproved()) {
            booking.setStatus(APPROVED);
        } else {
            booking.setStatus(REJECTED);
        }

        Booking bookingUpdated = bookingRepository.save(booking);

        return bookingMapper.mapToBookingResponse(bookingUpdated);
    }

    @Override
    @Transactional
    public void delete(long bookingId, long bookerId) {

        BookingOwnerId booker = bookingRepository.findOwnerIdByBookingId(bookingId)
                .orElseThrow(() -> new NotFoundException("Бронь не найдена")
        );

        if (booker.getBookerId() != bookerId) {
            throw new NotFoundException("Удалить бронь может только создатель брони");
        }

        bookingRepository.deleteById(bookingId);
    }

    @Override
    public List<BookingResponseDto> getAllBookingsByBookerId(long bookerId, BookingStatus status) {
        User booker = getUserIfExist(bookerId);

        List<Booking> list;

        list = switch (status) {
            case ALL -> bookingRepository.findAllByBookerId(bookerId, SORT_BY_START_BOOKING_DESC);
            case CURRENT -> bookingRepository.findAllCurrentByBookerIdAndStatusApproved(
                    bookerId, SORT_BY_START_BOOKING_DESC);
            case PAST -> bookingRepository.findAllByBookerIdPast(bookerId, SORT_BY_START_BOOKING_DESC);
            case FUTURE -> bookingRepository.findFutureByBookerId(bookerId, SORT_BY_START_BOOKING_DESC);
            case WAITING -> bookingRepository.findAllCurrentByBookerIdAndStatus(
                    bookerId, WAITING, SORT_BY_START_BOOKING_DESC);
            case REJECTED -> bookingRepository.findAllCurrentByBookerIdAndStatus(
                    bookerId, REJECTED, SORT_BY_START_BOOKING_DESC);
            default -> throw new ParameterNotValidException("Не поддерживаемое состояние:", status.name());
        };

        if (list.isEmpty()) return Collections.emptyList();

        return list.stream()
                .map(bookingMapper::mapToBookingResponse)
                .toList();
    }

    @Override
    public List<BookingResponseDto> getAllBookingsByOwnerId(long ownerId, BookingStatus status) {
        User owner = getUserIfExist(ownerId);

        List<Booking> list;

        list = switch (status) {
            case ALL -> bookingRepository.findAllByOwnerId(ownerId, SORT_BY_START_BOOKING_DESC);
            case CURRENT -> bookingRepository.findCurrentByOwnerId(ownerId, SORT_BY_START_BOOKING_DESC);
            case PAST -> bookingRepository.findPastByOwnerId(ownerId, SORT_BY_START_BOOKING_DESC);
            case FUTURE -> bookingRepository.findFutureByOwnerId(ownerId, SORT_BY_START_BOOKING_DESC);
            case WAITING -> bookingRepository.findAllCurrentByOwnerIdAndStatus(
                    ownerId, WAITING, SORT_BY_START_BOOKING_DESC);
            case REJECTED -> bookingRepository.findAllCurrentByOwnerIdAndStatus(
                    ownerId, REJECTED, SORT_BY_START_BOOKING_DESC);
            default -> throw new ParameterNotValidException("Не поддерживаемое состояние:", status.name());
        };

        if (list.isEmpty()) return Collections.emptyList();

        return list.stream()
                .map(bookingMapper::mapToBookingResponse)
                .toList();
    }

    @Transactional(propagation = Propagation.REQUIRED)
    private User getUserIfExist(long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователь не найден"));
    }
}
