package ru.practicum.shareit.review.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.FieldDefaults;
import ru.practicum.shareit.util.Marker;

@Getter
@Setter
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ReviewDto {

    @NotBlank(groups = Marker.OnCreate.class)
    String content;

    @NotNull(groups = Marker.OnCreate.class)
    @JsonProperty("isComplete")
    Boolean isComplete;

    @NotNull(groups = Marker.OnCreate.class)
    @JsonProperty("isPositive")
    Boolean isPositive;

    @NotNull(groups = Marker.OnCreate.class)
    Long itemId;

    @NotNull(groups = Marker.OnCreate.class)
    Long consumerId;

    @NotNull(groups = Marker.OnCreate.class)
    Long ownerId;
}
