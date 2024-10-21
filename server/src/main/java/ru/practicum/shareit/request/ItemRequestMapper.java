package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.item.dto.ItemShortResponseDto;
import ru.practicum.shareit.request.dto.ItemRequestCreateDto;
import ru.practicum.shareit.request.dto.ItemRequestResponseDto;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Component
@RequiredArgsConstructor
public final class ItemRequestMapper {

    public ItemRequest mapToItemRequest(ItemRequestCreateDto request) {

        return ItemRequest.builder()
                .requestor(User.builder().id(request.getRequestorId()).build())
                .description(request.getDescription())
                .created(LocalDateTime.now())
                .build();
    }

    public ItemRequestResponseDto mapToItemRequestDto(ItemRequest item, List<ItemShortResponseDto> items) {

        return new ItemRequestResponseDto(
                item.getId(),
                item.getDescription(),
                convertDateFormat(item.getCreated()),
                items
        );
    }

    public ItemRequestResponseDto mapToItemRequestDto(ItemRequest item) {

        return new ItemRequestResponseDto(
                item.getId(),
                item.getDescription(),
                convertDateFormat(item.getCreated()),
                null
        );
    }

    private String convertDateFormat(LocalDateTime localDateTime) {
        String dateTimeformatted = null;

        if (localDateTime != null) {
            dateTimeformatted = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")
                    .withZone(ZoneOffset.UTC)
                    .format(localDateTime);
        }
        return dateTimeformatted;
    }
}
