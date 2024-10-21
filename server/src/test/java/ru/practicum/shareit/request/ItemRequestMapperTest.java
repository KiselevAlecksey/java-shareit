package ru.practicum.shareit.request;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.practicum.shareit.item.dto.ItemShortResponseDto;
import ru.practicum.shareit.request.dto.ItemRequestCreateDto;
import ru.practicum.shareit.request.dto.ItemRequestResponseDto;
import ru.practicum.shareit.request.model.ItemRequest;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static ru.practicum.shareit.util.TestDataFactory.*;

@DisplayName("ItemRequestMapper")
class ItemRequestMapperTest {

    private final ItemRequestMapper itemRequestMapper = new ItemRequestMapper();

    @Test
    @DisplayName("Должен маппить дто в модель запроса")
    void should_map_to_item_request() {
        ItemRequestCreateDto createDto = new ItemRequestCreateDto();
        createDto.setRequestorId(TEST_USER_ID);
        createDto.setDescription("Описание запроса");

        ItemRequest itemRequest = itemRequestMapper.mapToItemRequest(createDto);

        assertEquals(TEST_USER_ID, itemRequest.getRequestor().getId(),
                "ID запрашивающего пользователя должен совпадать");
        assertEquals("Описание запроса", itemRequest.getDescription(),
                "Описание должно совпадать");
        assertEquals(NOW_DATE_TIME.getMinute(), itemRequest.getCreated().getMinute(),
                "Время создания должно быть установлено");
    }

    @Test
    @DisplayName("Должен маппить модель в дто со списком ItemShortResponseDto")
    void should_map_to_item_request_dto_with_items() {
        ItemRequest itemRequest = new ItemRequest();
        itemRequest.setId(TEST_ID_ONE);
        itemRequest.setDescription("Описание запроса");
        itemRequest.setCreated(NOW_DATE_TIME);
        String created = convertDatePattern(NOW_DATE_TIME);

        List<ItemShortResponseDto> items = Collections
                .singletonList(new ItemShortResponseDto(TEST_ID_ONE, "name", TEST_USER_ID));

        ItemRequestResponseDto itemRequestDto = itemRequestMapper.mapToItemRequestDto(itemRequest, items);

        assertEquals(1L, itemRequestDto.getId(), "ID должен совпадать");
        assertEquals("Описание запроса", itemRequestDto.getDescription(), "Описание должно совпадать");
        assertEquals(created, itemRequestDto.getCreated(), "Дата создания должна совпадать");
        assertEquals(1, itemRequestDto.getItems().size(), "Список предметов должен содержать 1 элемент");
    }

    @Test
    @DisplayName("Должен маппить модель в дто без списка ItemShortResponseDto")
    void should_map_to_item_request_dto_without_items() {
        ItemRequest itemRequest = new ItemRequest();
        itemRequest.setId(TEST_ID_ONE);
        itemRequest.setDescription("Описание запроса");
        itemRequest.setCreated(NOW_DATE_TIME);
        String created = convertDatePattern(NOW_DATE_TIME);

        ItemRequestResponseDto itemRequestDto = itemRequestMapper.mapToItemRequestDto(itemRequest);

        assertEquals(TEST_ID_ONE, itemRequestDto.getId(), "ID должен совпадать");
        assertEquals("Описание запроса", itemRequestDto.getDescription(), "Описание должно совпадать");
        assertEquals(created, itemRequestDto.getCreated(), "Дата создания должна совпадать");
        assertNull(itemRequestDto.getItems(), "Список предметов должен быть пустым");
    }
}