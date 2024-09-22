package ru.practicum.shareit.review.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UpdateReviewRequest implements ReviewRequest {

    Long id;

    @NotBlank
    String content;

    @JsonProperty("isComplete")
    Boolean isComplete;

    @JsonProperty("isPositive")
    Boolean isPositive;

    public boolean hasContent() {
        return content != null && !content.isBlank();
    }

    public boolean hasIsComplete() {
        return isComplete != null;
    }

    public boolean hasIsPositive() {
        return isPositive != null;
    }
}
