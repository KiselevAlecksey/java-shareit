package ru.practicum.shareit.review.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class NewReviewRequest implements ReviewRequest {

    Long id;

    @NotBlank
    String content;

    @NotNull
    @JsonProperty("isComplete")
    Boolean isComplete;

    @NotNull
    @JsonProperty("isPositive")
    Boolean isPositive;

    @NotNull
    Long itemId;

    @NotNull
    Long consumerId;

    @NotNull
    Long ownerId;
}
