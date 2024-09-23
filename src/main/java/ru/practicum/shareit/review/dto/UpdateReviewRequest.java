package ru.practicum.shareit.review.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import ru.practicum.shareit.validator.ValidBoolean;
import ru.practicum.shareit.validator.ValidString;

@Data
public class UpdateReviewRequest {

    @ValidString
    String content;

    @JsonProperty("isComplete")
    @ValidBoolean
    Boolean isComplete;

    @JsonProperty("isPositive")
    @ValidBoolean
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
