package ru.practicum.shareit.item;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.dto.CommentResponseDto;
import ru.practicum.shareit.item.dto.ItemResponseDto;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static ru.practicum.shareit.util.TestDataFactory.*;

@Transactional
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@SpringBootTest
@DisplayName("ItemService")
class ItemServiceImplTest {
    private final ItemService itemService;

    @Test
    @DisplayName("Должен возвращать все элементы")
    void should_get_all_items() {
        List<ItemResponseDto> dtoList = itemService.getAll(TEST_USER_ID);

        assertEquals(createItemDtoList(), dtoList, "Списки должны совпадать");
    }

    @Test
    @DisplayName("Должен создавать новый элемент")
    void should_create_item() {
        ItemResponseDto dto = itemService.create(itemCreateRequestOneDto());

        long testId = dto.getId();

        assertEquals(itemCreatedDto(testId), dto, "Предметы должны совпадать");
    }

    @Test
    @DisplayName("Должен обновлять элемент")
    void should_update_item() {
        ItemResponseDto dto = itemService.update(itemUpdateDto());

        assertEquals(itemUpdatedDto(), dto, "Предметы должны совпадать");
    }

    @Test
    @DisplayName("Должен удалять элемент")
    void should_delete_item() {
        itemService.delete(TEST_ITEM_ID);

        NotFoundException exception = assertThrows(NotFoundException.class, () -> {
                itemService.get(TEST_ITEM_ID);

        });

        assertEquals("Предмет не найден", exception.getMessage(), "Сообщение исключения должен совпадать");
    }

    @Test
    @DisplayName("Должен возвращать элемент по id")
    void should_get_item_by_id() {
        ItemResponseDto responseDto = itemService.get(TEST_ITEM_ID);

        assertEquals(itemCreatedDto(), responseDto, "Предметы должны совпадать");
    }

    @Test
    @DisplayName("Должен искать элементы по критериям")
    void should_search_items() {
        List<ItemResponseDto> dtoList = itemService.search("Книг");

        assertEquals(getItemDtoSearchList(), dtoList, "Списки должны совпадать");
    }

    @Test
    @DisplayName("Должен создавать комментарий к элементу")
    void should_create_comment() {
        CommentResponseDto responseDto = itemService.createComment(createCommentDto());

        long testId = responseDto.id();

        assertEquals(createdCommentDto(testId), responseDto, "Комментарий должен совпадать");
    }
}