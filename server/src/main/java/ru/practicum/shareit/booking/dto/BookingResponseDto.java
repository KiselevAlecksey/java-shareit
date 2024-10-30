package ru.practicum.shareit.booking.dto;

import ru.practicum.shareit.item.dto.ItemResponseDto;
import ru.practicum.shareit.user.dto.UserResponseDto;

public record BookingResponseDto(
        long id,

        ItemResponseDto item,

        String start,

        String end,

        UserResponseDto booker,

        String status,

        String confirmTime) {
}
