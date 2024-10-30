package ru.practicum.shareit.request;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.shareit.request.dto.ItemRequestResponseDto;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static ru.practicum.shareit.util.TestDataFactory.*;

@Transactional
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@SpringBootTest
@DisplayName("ItemRequestService")
class ItemRequestServiceImplTest {
    private final ItemRequestService itemRequestService;

    @Test
    @DisplayName("Должен возвращать все запросы пользователя по id")
    void should_get_all_requests_by_user_id() {

        List<ItemRequestResponseDto> responseDtoList = itemRequestService.getAllByUserId(TEST_USER_ID);

        assertEquals(createItemRequestDtoList(), responseDtoList, "Списки должны совпадать");
    }

    @Test
    @DisplayName("Должен возвращать все запросы")
    void should_get_all_requests() {

        List<ItemRequestResponseDto> responseDtoList = itemRequestService.getAll();

        assertEquals(createAllItemRequestDtoList(), responseDtoList, "Списки должны совпадать");
    }

    @Test
    @DisplayName("Должен создавать запрос")
    void should_create_request() {

        ItemRequestResponseDto createdDto = itemRequestService.create(createItemRequestDto());

        long testId = createdDto.getId();

        ItemRequestResponseDto expectedDto = createdItemRequestDto(testId, NOW_DATE_TIME, null);

        assertEquals(expectedDto.getId(), createdDto.getId(), "Запросы должны совпадать");
        assertEquals(expectedDto.getDescription(), createdDto.getDescription(), "Запросы должны совпадать");
        assertEquals(expectedDto.getItems(), createdDto.getItems(), "Запросы должны совпадать");
    }

    @Test
    @DisplayName("Должен возвращать запрос по id")
    void should_get_request_by_id() {
        ItemRequestResponseDto responseDto = itemRequestService.get(TEST_ITEM_REQUEST_ID);

        assertEquals(createdItemRequestDto(), responseDto, "Запросы должны совпадать");
    }
}