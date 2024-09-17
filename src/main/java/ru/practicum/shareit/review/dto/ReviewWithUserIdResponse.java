package ru.practicum.shareit.review.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ReviewWithUserIdResponse {

    Long id;

    @NotBlank
    String content;

    @NotNull
    @JsonProperty("isComplete")
    Boolean isComplete;

    @NotNull
    @JsonProperty("isPositive")
    Boolean isPositive;

    String useful;

    @NotNull
    Long consumerId;
}
