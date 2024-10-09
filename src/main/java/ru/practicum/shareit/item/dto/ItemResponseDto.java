package ru.practicum.shareit.item.dto;

import java.util.List;

public record ItemResponseDto(
        Long id,

        String name,

        String description,

        Boolean available,

        String lastBooking,

        String nextBooking,

        List<CommentResponseDto> comments) {
}
