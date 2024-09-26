package ru.practicum.shareit.booking.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UpdateBookingConfirmResponse {

    @NotNull
    @JsonProperty("isConfirm")
    Boolean isConfirm;

    public boolean hasIsConfirm() {
        return isConfirm != null && !isConfirm;
    }
}
