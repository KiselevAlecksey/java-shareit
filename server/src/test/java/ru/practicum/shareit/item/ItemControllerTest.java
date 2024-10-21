package ru.practicum.shareit.item;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.practicum.shareit.util.TestDataFactory.*;

@WebMvcTest(ItemController.class)
@AutoConfigureMockMvc
@DisplayName("ItemController")
class ItemControllerTest {
    @MockBean
    private ItemService itemService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private final ObjectMapper mapper = new ObjectMapper();

    @Test
    @DisplayName("Должен получить все предметы")
    void should_get_all_items() throws Exception {

        when(itemService.getAll(TEST_USER_ID)).thenReturn(createItemDtoList());

        mockMvc.perform(get("/items")
                .header(TEST_USER_ID_HEADER, TEST_USER_ID))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(createItemDtoList())));

        verify(itemService, times(1)).getAll(TEST_USER_ID);
    }

    @Test
    @DisplayName("Должен создать предмет")
    void should_create_item() throws Exception {

        when(itemService.create(itemCreateDto())).thenReturn(itemCreatedDto());

        mockMvc.perform(post("/items")
                        .content(mapper.writeValueAsString(itemCreateDto()))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(content().json(mapper.writeValueAsString(itemCreatedDto())));

        verify(itemService, times(1)).create(itemCreateDto());
    }

    @Test
    @DisplayName("Должен обновить предмет")
    void should_update_item() throws Exception {

        when(itemService.update(itemUpdateDto())).thenReturn(itemUpdatedDto());

        mockMvc.perform(patch("/items/{itemId}", TEST_ITEM_ID)
                        .content(mapper.writeValueAsString(itemUpdateDto()))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(itemUpdatedDto())));

        verify(itemService, times(1)).update(itemUpdateDto());
    }

    @Test
    @DisplayName("Должен удалить предмет")
    void should_delete_item() throws Exception {

        doNothing().when(itemService).delete(eq(TEST_ITEM_ID));

        mockMvc.perform(delete("/items/{itemId}", TEST_ITEM_ID))
                .andExpect(status().isOk())
                .andDo(print());

        verify(itemService, times(1)).delete(TEST_ITEM_ID);
    }

    @Test
    @DisplayName("Должен получить предмет")
    void should_get_item() throws Exception {
        when(itemService.get(TEST_ITEM_ID)).thenReturn(itemCreatedDto());

        mockMvc.perform(get("/items/{itemId}", TEST_ITEM_ID)
                .header(TEST_USER_ID_HEADER, TEST_USER_ID))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(itemCreatedDto())));

        verify(itemService, times(1)).get(TEST_ITEM_ID);
    }

    @Test
    @DisplayName("Должен найти все предметы по ключевому слову в названии или описании")
    void should_search_items() throws Exception {

        when(itemService.search("")).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/items/search")
                .header(TEST_USER_ID_HEADER, TEST_USER_ID)
                .content(""))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(Collections.emptyList())));

        verify(itemService, times(1)).search("");

    }

    @Test
    @DisplayName("Должен создать комментарий")
    void should_create_comment() throws Exception {

        when(itemService.createComment(createCommentDto())).thenReturn(createdCommentDto());

        mockMvc.perform(post("/items/{itemId}/comment", TEST_ITEM_ID)
                        .header(TEST_USER_ID_HEADER, TEST_USER_ID)
                        .content(mapper.writeValueAsString(createCommentDto()))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(content().json(mapper.writeValueAsString(createdCommentDto())));

        verify(itemService, times(1)).createComment(createCommentDto());
    }
}