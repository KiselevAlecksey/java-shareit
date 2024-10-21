package ru.practicum.shareit.item.mapper;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.practicum.shareit.item.ItemShort;
import ru.practicum.shareit.item.dto.ItemCreateDto;
import ru.practicum.shareit.item.dto.ItemResponseDto;
import ru.practicum.shareit.item.dto.ItemShortResponseDto;
import ru.practicum.shareit.item.dto.ItemUpdateDto;
import ru.practicum.shareit.item.model.Item;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static ru.practicum.shareit.util.TestDataFactory.*;

@DisplayName("ItemMapper")
public class ItemMapperTest {

    private final ItemMapper itemMapper = new ItemMapper(new CommentMapper());

    @Test
    @DisplayName("Должен маппить дто в модель запроса")
    public void should_map_to_item_request() {
        ItemCreateDto dto = new ItemCreateDto("Item Name", "Item Description", true, null, TEST_USER_ID);

        Item item = itemMapper.mapToItem(dto);

        assertEquals(TEST_USER_ID, item.getOwner().getId());
        assertEquals("Item Name", item.getName());
        assertEquals("Item Description", item.getDescription());
        assertTrue(item.getAvailable());
        assertNull(item.getRequestId());
    }

    @Test
    @DisplayName("Должен маппить модель в дто без бронирования")
    public void should_map_to_item_dto_without_booking() {
        Item item = new Item(TEST_ITEM_ID, createUser(), "Item Name", "Item Description", true, null);

        ItemResponseDto dto = itemMapper.mapToItemDto(item);

        assertEquals(TEST_ITEM_ID, dto.getId());
        assertEquals("Item Name", dto.getName());
        assertEquals("Item Description", dto.getDescription());
        assertTrue(dto.getAvailable());
        assertNull(dto.getLastBooking());
        assertNull(dto.getNextBooking());
        assertTrue(dto.getComments().isEmpty());
    }

    @Test
    @DisplayName("Должен маппить модель в короткое дто")
    public void should_map_to_item_short_dto() {
        ItemShort itemShort = new ItemShortResponseDto(TEST_ITEM_ID, "Item Name", TEST_USER_ID);

        ItemShortResponseDto dto = itemMapper.mapToItemShortDto(itemShort);

        assertEquals(TEST_ITEM_ID, dto.getId());
        assertEquals("Item Name", dto.getName());
        assertEquals(TEST_USER_ID, dto.getOwnerId());
    }

    @Test
    @DisplayName("Должен маппить модель в дто с бронированием")
    public void should_map_to_item_dto_with_booking() {
        Item item = new Item(TEST_ITEM_ID, createUser(), "Item Name", "Item Description", true, null);
        LocalDateTime lastBooking = LocalDateTime.now().minusDays(1);
        LocalDateTime nextBooking = LocalDateTime.now().plusDays(1);

        ItemResponseDto dto = itemMapper.mapToItemDto(item, lastBooking, nextBooking);

        assertEquals(TEST_ITEM_ID, dto.getId());
        assertEquals("Item Name", dto.getName());
        assertEquals("Item Description", dto.getDescription());
        assertTrue(dto.getAvailable());
        assertNotNull(dto.getLastBooking());
        assertNotNull(dto.getNextBooking());
    }

    @Test
    @DisplayName("Должен обновлять поля модели на основе дто")
    public void should_update_item_fields() {
        Item item = new Item(TEST_ITEM_ID, createUser(), "Old Name", "Old Description", true, null);
        ItemUpdateDto updateDto = new ItemUpdateDto(TEST_ITEM_ID, "New Name", "New Description", false, TEST_USER_ID);

        itemMapper.updateItemFields(item, updateDto);

        assertEquals("New Name", item.getName());
        assertEquals("New Description", item.getDescription());
        assertFalse(item.getAvailable());
    }

    @Test
    @DisplayName("Не должен обновлять пустые поля модели на основе дто")
    public void should_not_update_empty_fields() {
        Item item = new Item(TEST_ITEM_ID, createUser(), "Old Name", "Old Description", true, null);
        ItemUpdateDto updateDto = new ItemUpdateDto(TEST_ITEM_ID, null, "New Description", null, TEST_USER_ID); // Имя остается старым

        itemMapper.updateItemFields(item, updateDto);

        assertEquals("Old Name", item.getName());
        assertEquals("New Description", item.getDescription());
        assertTrue(item.getAvailable());
    }
}