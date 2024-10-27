package ru.practicum.shareit.request;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.practicum.shareit.util.TestDataFactory.*;
import static ru.practicum.shareit.util.TestDataFactory.createdItemRequestDtoListByUserId;

@WebMvcTest(ItemRequestController.class)
@AutoConfigureMockMvc
@DisplayName("ItemRequestController")
class ItemRequestControllerTest {
    @MockBean
    private ItemRequestService itemRequestService;

    @Autowired
    private MockMvc mockMvc;

    private final ObjectMapper mapper = new ObjectMapper();

    @Test
    @DisplayName("Должен создать запрос")
    void should_create_item_request() throws Exception {

        when(itemRequestService.create(createItemRequestDto())).thenReturn(createdItemRequestDto());

        mockMvc.perform(post("/requests")
                        .content(mapper.writeValueAsString(createItemRequestDto()))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());

        verify(itemRequestService, times(1)).create(createItemRequestDto());
    }

    @Test
    @DisplayName("Получить все запросы пользователя")
    void should_get_all_item_requests_by_user_id() throws Exception {
        when(itemRequestService.getAllByUserId(TEST_USER_ID)).thenReturn(createdItemRequestDtoListByUserId());

        mockMvc.perform(get("/requests")
                .header(TEST_USER_ID_HEADER, TEST_USER_ID))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(createdItemRequestDtoListByUserId())));

        verify(itemRequestService, times(1)).getAllByUserId(TEST_USER_ID);
    }

    @Test
    @DisplayName("Получить все запросы")
    void should_get_all() throws Exception {
        when(itemRequestService.getAll()).thenReturn(createItemRequestDtoList());

        mockMvc.perform(get("/requests/all"))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(createItemRequestDtoList())));

        verify(itemRequestService, times(1)).getAll();
    }

    @Test
    @DisplayName("Получить запрос по идентификатору")
    void should_get_by_id() throws Exception {
        when(itemRequestService.get(TEST_USER_ID)).thenReturn(createdItemRequestDto());

        mockMvc.perform(get("/requests/{requestId}", TEST_ID_ONE))
                .andExpect(status().isOk())
                .andDo(print());

        verify(itemRequestService, times(1)).get(TEST_ID_ONE);
    }
}