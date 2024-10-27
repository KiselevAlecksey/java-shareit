package ru.practicum.shareit.booking;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.text.SimpleDateFormat;
import java.util.Collections;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.practicum.shareit.util.TestDataFactory.*;

@WebMvcTest(BookingController.class)
@AutoConfigureMockMvc
@DisplayName("BookingController")
class BookingControllerTest {
    @MockBean
    private BookingService bookingService;

    @Autowired
    private MockMvc mockMvc;

    private final ObjectMapper mapper = new ObjectMapper();

    @BeforeEach
    void init() {
        mapper.registerModule(new JavaTimeModule());
        mapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss"));
    }

    @Test
    @DisplayName("Доблжен создать бронирование")
    void should_create_booking() throws Exception {

        when(bookingService.create(bookingCreateDto())).thenReturn(bookingCreatedDto());

        mockMvc.perform(post("/bookings")
                        .header(TEST_USER_ID_HEADER, TEST_USER_ID)
                        .content(mapper.writeValueAsString(bookingCreateDto()))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(content().json(mapper.writeValueAsString(bookingCreatedDto())));

        verify(bookingService, times(1)).create(bookingCreateDto());
    }

    @Test
    @DisplayName("Должен получить бронирование")
    void should_get_by_id() throws Exception {

        when(bookingService.getById(TEST_BOOKING_ID, TEST_USER_ID)).thenReturn(bookingCreatedDto());

        mockMvc.perform(get("/bookings/{bookingId}", TEST_BOOKING_ID)
                .header(TEST_USER_ID_HEADER, TEST_USER_ID))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(bookingCreatedDto())));

        verify(bookingService, times(1)).getById(TEST_BOOKING_ID, TEST_USER_ID);
    }

    @Test
    @DisplayName("Должен обновить бронирование")
    void should_update_booking() throws Exception {
        when(bookingService.update(bookingUpdateDto())).thenReturn(bookingUpdatedDto());

        mockMvc.perform(patch("/bookings/{bookerId}/{bookingId}", TEST_USER_ID, TEST_BOOKING_ID)
                        .header(TEST_USER_ID_HEADER, TEST_USER_ID)
                        .content(mapper.writeValueAsString(bookingUpdateDto()))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(bookingUpdatedDto())));

        verify(bookingService, times(1)).update(bookingUpdateDto());
    }

    @Test
    @DisplayName("Должен одобрить бронирование")
    void should_approve_booking() throws Exception {
        when(bookingService.approve(bookingApproveDto())).thenReturn(bookingUpdatedDto());

        mockMvc.perform(patch("/bookings/{bookingId}", TEST_BOOKING_ID)
                        .header(TEST_USER_ID_HEADER, TEST_USER_ID)
                        .content(mapper.writeValueAsString(bookingApproveDto()))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(bookingUpdatedDto())));

        verify(bookingService, times(1)).approve(bookingApproveDto());
    }

    @Test
    @DisplayName("Должен удалить бронирование")
    void should_delete_booking() throws Exception {

        doNothing().when(bookingService).delete(TEST_BOOKING_ID, TEST_USER_ID);

        mockMvc.perform(delete("/bookings/{bookingId}", TEST_USER_ID, TEST_BOOKING_ID)
                        .header(TEST_USER_ID_HEADER, TEST_USER_ID))
                .andExpect(status().isOk())
                .andDo(print());

        verify(bookingService, times(1)).delete(TEST_BOOKING_ID, TEST_USER_ID);
    }

    @Test
    @DisplayName("Должен получить список бронирований пользователя по статусу")
    void should_get_all_bookings_by_booker_id() throws Exception {

        String state = "APPROVED";

        BookingStatus status = BookingStatus.from(state)
                .orElseThrow(() -> new IllegalArgumentException("Не поддерживаемое состояние"));

        when(bookingService.getAllBookingsByBookerId(TEST_USER_ID, status))
                .thenReturn(Collections.emptyList());

        mockMvc.perform(get("/bookings")
                        .header(TEST_USER_ID_HEADER, TEST_USER_ID)
                        .param("state", state))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));

        verify(bookingService, times(1)).getAllBookingsByBookerId(TEST_USER_ID, status);
    }

    @Test
    @DisplayName("Должен получить список бронирований владельца по статусу")
    void should_get_all_bookings_by_owner_id() throws Exception {

        String state = "APPROVED";

        BookingStatus status = BookingStatus.from(state)
                .orElseThrow(() -> new IllegalArgumentException("Не поддерживаемое состояние"));

        when(bookingService.getAllBookingsByOwnerId(TEST_USER_ID, status))
                .thenReturn(Collections.emptyList());

        mockMvc.perform(get("/bookings/owner")
                        .header(TEST_USER_ID_HEADER, TEST_USER_ID)
                        .param("state", state))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));

        verify(bookingService, times(1)).getAllBookingsByOwnerId(TEST_USER_ID, status);
    }
}