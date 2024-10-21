package ru.practicum.shareit.request;

import ru.practicum.shareit.request.dto.ItemRequestCreateDto;
import ru.practicum.shareit.request.dto.ItemRequestResponseDto;

import java.util.List;

public interface ItemRequestService {

    List<ItemRequestResponseDto> getAllByUserId(long userId);

    List<ItemRequestResponseDto> getAll();

    ItemRequestResponseDto create(ItemRequestCreateDto itemRequestCreateDto);

    ItemRequestResponseDto get(long requestId);
}
