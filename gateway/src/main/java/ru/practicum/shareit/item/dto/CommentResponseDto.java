package ru.practicum.shareit.item.dto;

public record CommentResponseDto(
        long id,

        String authorName,

        String text,

        String created) {
}
