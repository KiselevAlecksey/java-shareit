package ru.practicum.shareit.booking.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UpdateBookingConfirmResponse implements BookingRequest {

    Long id;

    @NotBlank
    Long ownerId;

    @NotNull
    @JsonProperty("isConfirm")
    Boolean isConfirm;

    public boolean hasIsConfirm() {
        return isConfirm != null && !isConfirm;
    }
}
