package ru.practicum.shareit.item.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
public class ItemResponseDto {
        long id;

        String name;

        String description;

        Boolean available;

        String lastBooking;

        String nextBooking;

        List<CommentResponseDto> comments;
}
