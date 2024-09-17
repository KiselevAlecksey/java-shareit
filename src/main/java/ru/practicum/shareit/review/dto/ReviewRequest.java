package ru.practicum.shareit.review.dto;

public interface ReviewRequest {
    Boolean getIsPositive();

    String getContent();

    default Long getConsumerId() {
        return null;
    }

    default Long getOwnerId() {
        return null;
    }

    default Long getItemId() {
        return null;
    }
}
