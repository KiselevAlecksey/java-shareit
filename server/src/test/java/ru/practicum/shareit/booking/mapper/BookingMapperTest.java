package ru.practicum.shareit.booking.mapper;

import static org.junit.jupiter.api.Assertions.*;
import static ru.practicum.shareit.util.TestDataFactory.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.practicum.shareit.booking.BookingStatus;
import ru.practicum.shareit.booking.dto.BookingCreateDto;
import ru.practicum.shareit.booking.dto.BookingResponseDto;
import ru.practicum.shareit.booking.dto.BookingUpdateDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.mapper.CommentMapper;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;

import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

@DisplayName("BookingMapper")
public class BookingMapperTest {

    private final UserMapper userMapper = new UserMapper();
    private final CommentMapper commentMapper = new CommentMapper();
    private final ItemMapper itemMapper = new ItemMapper(commentMapper);
    private final BookingMapper bookingMapper = new BookingMapper(userMapper, itemMapper, commentMapper);

    @Test
    @DisplayName("Должен маппить BookingCreateDto в Booking")
    public void should_map_to_booking() {
        BookingCreateDto dto = bookingCreateDto();
        dto.setStart(NOW_DATE_TIME);
        dto.setEnd(NOW_DATE_TIME.plusSeconds(3600));

        Booking booking = bookingMapper.mapToBooking(dto);

        assertNotNull(booking);
        assertTrue(booking.getAvailable());
        assertEquals(dto.getStart(), booking.getStartBooking());
        assertEquals(dto.getEnd(), booking.getEndBooking());
    }

    @Test
    @DisplayName("Должен маппить Booking в BookingResponseDto")
    public void should_map_to_booking_response_dto() {

        User booker = createUser();
        booker.setName("Booker Name");

        Item item = itemCreate();
        item.setName("Item Name");

        Booking booking = new Booking(
                TEST_BOOKING_ID, createUser(), item, true, NOW_DATE_TIME,
                NOW_DATE_TIME.plusSeconds(3600), booker, NOW_DATE_TIME.plusDays(1), null, BookingStatus.APPROVED);

        BookingResponseDto dto = bookingMapper.mapToBookingResponse(booking);

        assertEquals(TEST_BOOKING_ID, dto.id());
        assertEquals("Item Name", dto.item().getName());
        assertEquals("Booker Name", dto.booker().name());

        String expectedStart = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")
                .withZone(ZoneOffset.UTC)
                .format(booking.getStartBooking());
        assertEquals(expectedStart, dto.start());

        String expectedEnd = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")
                .withZone(ZoneOffset.UTC)
                .format(booking.getEndBooking());
        assertEquals(expectedEnd, dto.end());

        String expectedAvailableTime = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")
                .withZone(ZoneOffset.UTC)
                .format(booking.getConfirmTime());
        assertEquals(expectedAvailableTime, dto.confirmTime());
    }

    @Test
    @DisplayName("Должен обновлять поля Booking согласно BookingUpdateDto")
    public void should_update_booking_fields() {
        Booking booking = new Booking(
                TEST_BOOKING_ID, createUser(), null, true, NOW_DATE_TIME,
                NOW_DATE_TIME.plusSeconds(3600), null, NOW_DATE_TIME.plusDays(1), null, BookingStatus.APPROVED);

        BookingUpdateDto updateDto = new BookingUpdateDto();
        updateDto.setStart(NOW_DATE_TIME.plusSeconds(7200));
        updateDto.setEnd(NOW_DATE_TIME.plusSeconds(10800));
        updateDto.setContent("Updated content");

        Booking updatedBooking = bookingMapper.updateBookingFields(booking, updateDto);

        assertEquals(updateDto.getStart(), updatedBooking.getStartBooking());
        assertEquals(updateDto.getEnd(), updatedBooking.getEndBooking());
        assertEquals("Updated content", updatedBooking.getContent());
    }

    @Test
    @DisplayName("Не должен обновлять пустые поля Booking согласно BookingUpdateDto")
    public void should_not_update_empty_fields() {
        Booking booking = new Booking(
                TEST_BOOKING_ID, createUser(), null, true, NOW_DATE_TIME,
                NOW_DATE_TIME.plusSeconds(3600), null, NOW_DATE_TIME.plusDays(1), null, BookingStatus.APPROVED);

        BookingUpdateDto updateDto = new BookingUpdateDto();
        updateDto.setStart(NOW_DATE_TIME.plusSeconds(7200));
        updateDto.setEnd(null);
        updateDto.setContent(null);

        Booking updatedBooking = bookingMapper.updateBookingFields(booking, updateDto);

        assertEquals(updateDto.getStart(), updatedBooking.getStartBooking());
        assertEquals(booking.getEndBooking(), updatedBooking.getEndBooking());
        assertEquals(booking.getContent(), updatedBooking.getContent());
    }
}