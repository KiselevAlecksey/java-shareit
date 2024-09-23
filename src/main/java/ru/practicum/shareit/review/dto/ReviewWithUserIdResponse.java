package ru.practicum.shareit.review.dto;

public record ReviewWithUserIdResponse(

    Long id,

    String content,

    Boolean isComplete,

    Boolean isPositive,

    String useful,

    Long consumerId) {
}
