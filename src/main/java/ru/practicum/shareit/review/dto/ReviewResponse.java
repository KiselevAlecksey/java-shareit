package ru.practicum.shareit.review.dto;

public record ReviewResponse(

    Long id,

    String content,

    Boolean isComplete,

    Boolean isPositive,

    String useful) {
}
