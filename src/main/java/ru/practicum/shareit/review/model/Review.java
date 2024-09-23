package ru.practicum.shareit.review.model;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.FieldDefaults;

@Data
@Builder(toBuilder = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
@EqualsAndHashCode(of = "id")
public class Review {

    Long id;

    String content;

    Long ownerId;

    Boolean isComplete;

    Boolean isPositive;

    String useful;

    Long consumerId;

    Long itemId;
}
