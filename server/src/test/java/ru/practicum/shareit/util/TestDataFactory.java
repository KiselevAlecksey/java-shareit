package ru.practicum.shareit.util;

import lombok.NoArgsConstructor;
import ru.practicum.shareit.booking.dto.BookingApproveDto;
import ru.practicum.shareit.booking.dto.BookingCreateDto;
import ru.practicum.shareit.booking.dto.BookingResponseDto;
import ru.practicum.shareit.booking.dto.BookingUpdateDto;
import ru.practicum.shareit.item.dto.*;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.dto.ItemRequestCreateDto;
import ru.practicum.shareit.request.dto.ItemRequestResponseDto;
import ru.practicum.shareit.user.dto.UserCreateDto;
import ru.practicum.shareit.user.dto.UserResponseDto;
import ru.practicum.shareit.user.dto.UserUpdateDto;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@NoArgsConstructor
public class TestDataFactory {
    public static final long TEST_USER_ID = 1L;
    public static final long TEST_USER2_ID = 2L;
    public static final long TEST_USER3_ID = 3L;

    public static final long TEST_ITEM_REQUEST_ID = 1L;
    public static final long TEST_ITEM_REQUEST2_ID = 2L;
    public static final long TEST_ITEM_REQUEST3_ID = 3L;

    public static final long TEST_ITEM_ID = 1L;
    public static final long TEST_ITEM2_ID = 2L;
    public static final long TEST_ITEM3_ID = 3L;

    public static final long TEST_ID_ONE = 1L;
    public static final long TEST_ID_TWO = 2L;
    public static final long TEST_ID_THREE = 3L;
    public static final long TEST_ID_FOUR = 4L;

    public static final long TEST_BOOKING_ID = 1L;
    public static final long TEST_BOOKING2_ID = 2L;
    public static final long TEST_BOOKING3_ID = 3L;

    public static final LocalDateTime START_BOOKING = LocalDateTime.of(2024, 10, 28, 12, 0);
    public static final LocalDateTime END_BOOKING = LocalDateTime.of(2024, 10, 28, 13, 0);
    public static final String START = "2024-10-28T12:00:00";
    public static final String END = "2024-10-28T13:00:00";
    public static final String CONFIRM_TIME = "2024-10-29T12:00:00";
    public static final String CONFIRM_UPDATED_TIME = "2023-10-02T12:00:00";
    public static final LocalDateTime START_BOOK = LocalDateTime.of(2023, 10, 1, 12, 0);
    public static final LocalDateTime END_BOOK = LocalDateTime.of(2023, 10, 15, 12, 0);

    public static final String TEST_USER_ID_HEADER = "X-Sharer-User-Id";
    public static final long ONE_DAY_IN_MILLIS = 86_400_000L;
    public static final long ONE_DAY_IN_MINUTES = 1440L;
    public static final LocalDateTime NOW_DATE_TIME = LocalDateTime.now();

    //users

    public static User createUser() {
        return new User(TEST_USER_ID, "Иван Иванов", "ivan@example.com");
    }

    public static UserCreateDto createUserDto() {
        return new UserCreateDto("Иван Иванов", "ivan@example.com");
    }

    public static UserCreateDto createUserDto(String name, String description) {
        return new UserCreateDto(name, description);
    }

    public static UserResponseDto createdUserDto(Long userId, String name, String description) {
        return new UserResponseDto(userId, name, description);
    }

    public static UserResponseDto createdUserDto(String name, String description) {
        return new UserResponseDto(TEST_USER_ID, name, description);
    }

    public static UserResponseDto createdUserDto() {
        return new UserResponseDto(TEST_USER_ID, "Иван Иванов", "ivan@example.com");
    }

    public static UserResponseDto createdUser2Dto() {
        return new UserResponseDto(TEST_USER2_ID, "Петр Петров", "petr@example.com");
    }

    public static UserUpdateDto updateUserDto() {
        return new UserUpdateDto(TEST_USER_ID, "Иван Иванов", "ivan@example.ru");
    }

    public static UserUpdateDto updateUserDto(Long userId, String name, String description) {
        return new UserUpdateDto(userId, name, description);
    }

    public static UserUpdateDto updateUserDto(String name, String description) {
        return new UserUpdateDto(TEST_USER_ID, name, description);
    }

    public static UserResponseDto updatedUserDto(Long userId, String name, String description) {
        return new UserResponseDto(userId, name, description);
    }

    public static UserResponseDto updatedUserDto() {
        return new UserResponseDto(TEST_USER_ID, "Иван Иванов", "ivan@example.ru");
    }

    public static List<UserResponseDto> createUserDtoList() {
        List<UserResponseDto> list = new ArrayList<>(3);
        list.add(new UserResponseDto(TEST_USER_ID,"Иван Иванов", "ivan@example.com"));
        list.add(new UserResponseDto(TEST_USER2_ID,"Петр Петров", "petr@example.com"));
        list.add(new UserResponseDto(TEST_USER3_ID,"Анна Сидорова", "anna@example.com"));

        return list;
    }

    //itemRequests

    public static ItemRequestCreateDto createItemRequestDto() {
        return new ItemRequestCreateDto(TEST_USER_ID, "Хочу почитать эту книгу.");
    }

    public static ItemRequestResponseDto createdItemRequestDto() {
        return new ItemRequestResponseDto(
                TEST_ITEM_REQUEST_ID, "Хочу почитать эту книгу.",
                convertDatePattern(LocalDateTime.of(2023, 9, 29, 15, 0)), createItemShortDtoList());
    }

    public static ItemRequestResponseDto createdItemRequestDto(
            Long requestId, LocalDateTime created, List<ItemShortResponseDto> items) {
        return new ItemRequestResponseDto(
                requestId, "Хочу почитать эту книгу.", convertDatePattern(created), items);
    }

    public static ItemRequestResponseDto createdItemRequestDto(LocalDateTime created) {
        return new ItemRequestResponseDto(
                TEST_ITEM_REQUEST_ID, "Хочу почитать эту книгу.", convertDatePattern(created), createItem3ShortDtoList());
    }

    public static List<ItemRequestResponseDto> createAllItemRequestDtoList() {
        List<ItemRequestResponseDto> list = new ArrayList<>(3);
        list.add(new ItemRequestResponseDto(
                TEST_ID_ONE, "Хочу почитать эту книгу.", "2023-09-29T15:00:00", createItem3ShortDtoList()));
        list.add(new ItemRequestResponseDto(
                TEST_ID_TWO, "Нужно взять ноутбук на неделю.", "2023-11-03T19:00:00", createItem3ShortDtoList()));
        list.add(new ItemRequestResponseDto(
                TEST_ID_THREE, "Планирую покататься на выходных.", "2023-11-28T22:00:00", createItem3ShortDtoList()));

        return list.reversed();
    }

    public static List<ItemRequestResponseDto> createAllItemRequestDtoWithoutItemList() {
        List<ItemRequestResponseDto> list = new ArrayList<>(3);
        list.add(new ItemRequestResponseDto(
                TEST_ID_ONE, "Хочу почитать эту книгу.", "2023-09-29T15:00:00", null));
        list.add(new ItemRequestResponseDto(
                TEST_ID_TWO, "Нужно взять ноутбук на неделю.", "2023-11-03T19:00:00", null));
        list.add(new ItemRequestResponseDto(
                TEST_ID_THREE, "Планирую покататься на выходных.", "2023-11-28T22:00:00", null));

        return list.reversed();
    }

    public static List<ItemRequestResponseDto> createItemRequestDtoList() {
        List<ItemRequestResponseDto> list = new ArrayList<>(3);

        list.add(new ItemRequestResponseDto(
                TEST_ID_THREE, "Планирую покататься на выходных.", "2023-11-28T22:00:00", createItem3ShortDtoList()));

        return list;
    }

    public static List<ItemRequestResponseDto> createdItemRequestDtoListByUserId() {
        List<ItemRequestResponseDto> list = new ArrayList<>();
        list.add(new ItemRequestResponseDto(
                TEST_ID_ONE, "Хочу почитать эту книгу.", "2023-10-25 14:00:00", createItem3ShortDtoList()));
        return list;
    }

    //items

    public static Item itemCreate() {
        return new Item(TEST_ID_ONE, createUser(), "Книги", "Сборник рассказов", true, null);
    }

    public static ItemCreateDto itemCreateDto() {
        return new ItemCreateDto("Книги", "Сборник рассказов", true, TEST_ID_FOUR, TEST_USER_ID);
    }

    public static ItemResponseDto itemCreatedDto() {
        return new ItemResponseDto(TEST_ID_ONE, "Книги",
                "Сборник рассказов", true, null, null, createCommentBookDtoList());
    }

    public static ItemResponseDto itemShortCreatedDto() {
        return new ItemResponseDto(TEST_ID_ONE, "Книги",
                "Сборник рассказов", true, null, null, Collections.emptyList());
    }

    public static ItemResponseDto itemShort2CreatedDto() {
        return new ItemResponseDto(TEST_ITEM3_ID, "Велосипед",
                "Горный велосипед", true, null, null, Collections.emptyList());
    }

    public static ItemResponseDto itemCreatedDto(Long itemId) {
        return new ItemResponseDto(itemId, "Книги", "Сборник рассказов", true, null, null, Collections.emptyList());
    }

    public static ItemUpdateDto itemUpdateDto() {
        return new ItemUpdateDto(TEST_ID_ONE, "Книги новые", "Сборник рассказов", false, TEST_USER_ID);
    }

    public static ItemResponseDto itemUpdatedDto() {
        return new ItemResponseDto(
                TEST_ID_ONE, "Книги новые", "Сборник рассказов", false, null, null, Collections.emptyList());
    }

    public static List<ItemResponseDto> createAllItemDtoList() {
        List<ItemResponseDto> list = new ArrayList<>(3);
        list.add(new ItemResponseDto(
                TEST_ITEM_ID, "Книги", "Сборник рассказов", true,
                "2023-09-30 14:00:00", "2023-10-30 14:00:00", createCommentDtoList()));
        list.add(new ItemResponseDto(
                TEST_ITEM2_ID, "Ноутбук", "MacBook Pro", false,
                "2023-09-30 14:00:00", "2023-10-30 14:00:00", createCommentDtoList()));
        list.add(new ItemResponseDto(
                TEST_ITEM3_ID, "Велосипед", "Горный велосипед", true,
                "2023-09-30 14:00:00", "2023-10-30 14:00:00", createCommentDtoList()));

        return list;
    }

    public static List<ItemResponseDto> createItemDtoList() {
        List<ItemResponseDto> list = new ArrayList<>();

        list.add(new ItemResponseDto(
                TEST_ITEM_ID, "Книги", "Сборник рассказов", true,
                "2023-10-15T12:00:00", null, Collections.emptyList()));

        return list;
    }

    public static List<ItemResponseDto> getItemDtoSearchList() {
        List<ItemResponseDto> list = new ArrayList<>();

        list.add(new ItemResponseDto(
                TEST_ITEM_ID, "Книги", "Сборник рассказов", true,
                null, null, Collections.emptyList()));

        return list;
    }

    public static List<ItemShortResponseDto> createAllItemShortDtoList() {
        List<ItemShortResponseDto> list = new ArrayList<>(3);
        list.add(new ItemShortResponseDto(
                TEST_ITEM_ID, "Книги", TEST_USER_ID));
        list.add(new ItemShortResponseDto(
                TEST_ITEM2_ID, "Ноутбук", TEST_USER2_ID));
        list.add(new ItemShortResponseDto(
                TEST_ITEM3_ID, "Велосипед", TEST_USER3_ID));

        return list;
    }

    public static List<ItemShortResponseDto> createItemShortDtoList() {
        List<ItemShortResponseDto> list = new ArrayList<>();

        list.add(new ItemShortResponseDto(
                TEST_ITEM_ID, "Книги", TEST_USER_ID));

        return list;
    }

    public static List<ItemShortResponseDto> createItem3ShortDtoList() {
        List<ItemShortResponseDto> list = new ArrayList<>();

        list.add(new ItemShortResponseDto(
                TEST_ITEM3_ID, "Велосипед", TEST_USER3_ID));

        return list;
    }

    //comments

    public static List<CommentResponseDto> createCommentDtoList() {
        List<CommentResponseDto> list = new ArrayList<>(3);
        list.add(new CommentResponseDto(
                TEST_ID_ONE, "Иван Иванов", "Велосипедом доволен, спасибо!", "2023-12-08 12:00:00"));
        list.add(new CommentResponseDto(
                TEST_ID_TWO, "Петр Петров", "Отличная книга!", "2023-10-16 10:00:00"));
        list.add(new CommentResponseDto(
                TEST_ID_THREE, "Анна Сидорова", "Очень удобный ноутбук.", "2023-11-21 11:00:00"));

        return list;
    }

    public static CommentCreateDto createCommentDto() {
        return new CommentCreateDto("Велосипедом доволен, спасибо!", TEST_USER2_ID, TEST_ITEM_ID);
    }

    public static CommentResponseDto createdCommentDto() {
        return new CommentResponseDto(
                TEST_ID_ONE, "Иван Иванов", "Велосипедом доволен, спасибо!",
                convertDatePattern(NOW_DATE_TIME));
    }

    public static CommentResponseDto createdCommentDto(Long commentId) {
        return new CommentResponseDto(
                commentId, "Петр Петров", "Велосипедом доволен, спасибо!",
                convertDatePattern(LocalDateTime.now()));
    }

    public static CommentResponseDto createdBookCommentDto(Long commentId) {
        return new CommentResponseDto(
                commentId, "Петр Петров", "Отличная книга!",
                convertDatePattern(LocalDateTime.of(2023, 10, 16, 7, 0)));
    }

    public static CommentResponseDto createdBookCommentDto() {
        return new CommentResponseDto(
                TEST_ID_ONE, "Петр Петров", "Отличная книга!",
                convertDatePattern(LocalDateTime.of(2023, 10, 16, 7, 0)));
    }

    public static List<CommentResponseDto> createCommentBookDtoList() {
        List<CommentResponseDto> list = new ArrayList<>(3);

        list.add(new CommentResponseDto(
                TEST_ID_ONE, "Петр Петров", "Отличная книга!",
                convertDatePattern(LocalDateTime.of(2023, 10, 16, 10, 0))));

        return list;
    }

    //booking

    public static BookingCreateDto bookingCreateDto() {
        return new BookingCreateDto(
                TEST_ITEM_ID, START_BOOKING, END_BOOKING, TEST_USER_ID, "", itemCreatedDto());
    }

    public static BookingResponseDto bookingCreatedDto() {
        return new BookingResponseDto(
                TEST_ID_ONE, itemShortCreatedDto(),
                "2023-10-01T12:00:00", "2023-10-15T12:00:00",
                createdUser2Dto(), "APPROVED", "2023-10-02T12:00:00");
    }

    public static BookingResponseDto bookingCreatedDto(Long bookingId) {
        return new BookingResponseDto(
                bookingId, itemShortCreatedDto(), START, END, createdUserDto(), "WAITING", CONFIRM_TIME);
    }

    public static BookingUpdateDto bookingUpdateDto() {
        return new BookingUpdateDto(TEST_BOOKING_ID, TEST_ITEM_ID, START_BOOKING, END_BOOKING, TEST_USER2_ID, "");
    }

    public static BookingResponseDto bookingUpdatedDto() {
        return new BookingResponseDto(
                TEST_ID_ONE, itemShortCreatedDto(), START, END, createdUser2Dto(), "APPROVED", CONFIRM_UPDATED_TIME);
    }

    public static BookingResponseDto bookingOneUpdatedDto() {
        return new BookingResponseDto(
                TEST_ID_ONE, itemShortCreatedDto(),
                "2023-10-01T12:00:00", "2023-10-15T12:00:00",
                createdUser2Dto(), "APPROVED", CONFIRM_UPDATED_TIME);
    }

    public static BookingApproveDto bookingApproveDto() {
        return new BookingApproveDto(TEST_BOOKING_ID, TEST_ID_ONE, true);
    }

    public static List<BookingResponseDto> bookingCreatedDtoList() {
        List<BookingResponseDto> dtoList = new ArrayList<>();

        dtoList.add(new BookingResponseDto(
                TEST_ID_THREE, itemShort2CreatedDto(),
                "2023-12-02T09:00:00", "2023-12-07T17:00:00",
                createdUserDto(), "REJECTED", "2023-12-01T13:00:00"));

        return dtoList;
    }

    public static List<BookingResponseDto> bookingCreatedDtoList2() {
        List<BookingResponseDto> dtoList = new ArrayList<>();

        dtoList.add(new BookingResponseDto(
                TEST_ID_ONE, itemShortCreatedDto(),
                "2023-10-01T12:00:00", "2023-10-15T12:00:00",
                createdUser2Dto(), "APPROVED", "2023-10-02T12:00:00"));

        return dtoList;
    }

    public static String convertDatePattern(LocalDateTime localDateTime) {
        String dateTimeformatted = null;

        if (localDateTime != null) {
            dateTimeformatted = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")
                    .withZone(ZoneOffset.UTC)
                    .format(localDateTime);
        }
        return dateTimeformatted;
    }
}
