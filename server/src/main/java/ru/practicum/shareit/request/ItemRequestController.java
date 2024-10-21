package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.ItemRequestCreateDto;
import ru.practicum.shareit.request.dto.ItemRequestResponseDto;

import java.util.List;

import static ru.practicum.shareit.util.Const.USER_ID_HEADER;

@RestController
@RequestMapping(path = "/requests")
@RequiredArgsConstructor
public class ItemRequestController {
    final ItemRequestService itemRequestService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ItemRequestResponseDto create(@RequestBody ItemRequestCreateDto createDto) {
        ItemRequestResponseDto responseDto = itemRequestService.create(createDto);
        return responseDto;
    }

    @GetMapping
    public List<ItemRequestResponseDto> getAllByUserId(@RequestHeader(USER_ID_HEADER) long requestorId) {
        List<ItemRequestResponseDto> responseDto = itemRequestService.getAllByUserId(requestorId);
        return responseDto;
    }

    @GetMapping("/all")
    public List<ItemRequestResponseDto> getAll() {
        List<ItemRequestResponseDto> responseDto = itemRequestService.getAll();
        return responseDto;
    }

    @GetMapping("/{requestId}")
    public ItemRequestResponseDto getById(@PathVariable long requestId) {
        ItemRequestResponseDto responseDto = itemRequestService.get(requestId);
        return responseDto;
    }
}
