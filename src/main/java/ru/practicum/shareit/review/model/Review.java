package ru.practicum.shareit.review.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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

    @NotBlank
    String content;

    @NotNull
    Long ownerId;

    @NotNull
    @JsonProperty("isComplete")
    Boolean isComplete;

    @NotNull
    @JsonProperty("isPositive")
    Boolean isPositive;

    String useful;

    @NotNull
    Long consumerId;

    @NotNull
    Long itemId;
}
