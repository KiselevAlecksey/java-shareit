package ru.practicum.shareit.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.nio.charset.StandardCharsets;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.practicum.shareit.util.TestDataFactory.*;

@WebMvcTest(UserController.class)
@AutoConfigureMockMvc
@DisplayName("UserController")
class UserControllerTest {
    @MockBean
    private UserService userService;

    @Autowired
    private MockMvc mockMvc;

    private final ObjectMapper mapper = new ObjectMapper();

    @Test
    @DisplayName("Должен получить всех пользователей")
    void should_find_all_users() throws Exception {
        when(userService.findAll()).thenReturn(createUserDtoList());

        mockMvc.perform(get("/users"))
                .andExpect(status().isOk())
                .andDo(print());

        verify(userService, times(1)).findAll();
    }

    @Test
    @DisplayName("Должен получить пользователя по id")
    void should_get_by_id_user() throws Exception {

        when(userService.getById(TEST_USER_ID)).thenReturn(createdUserDto());

        mockMvc.perform(get("/users/{userId}", TEST_USER_ID))
                .andExpect(status().isOk())
                .andDo(print());

        verify(userService, times(1)).getById(TEST_USER_ID);
    }

    @Test
    @DisplayName("Должен создать пользователя")
    void should_create_user() throws Exception {

        when(userService.create(createUserDto())).thenReturn(createdUserDto());

        mockMvc.perform(post("/users")
                                .header(TEST_USER_ID_HEADER, TEST_USER_ID)
                                .content(mapper.writeValueAsString(createUserDto()))
                                .characterEncoding(StandardCharsets.UTF_8)
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andDo(print());

        verify(userService, times(1)).create(createUserDto());
    }

    @Test
    @DisplayName("Должен обновить пользователя")
    void should_update_user() throws Exception {

        when(userService.update(updateUserDto())).thenReturn(updatedUserDto());

        mockMvc.perform(patch("/users/{userId}", TEST_USER_ID)
                .content(mapper.writeValueAsString(updateUserDto()))
                .characterEncoding(StandardCharsets.UTF_8)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print());

        verify(userService, times(1)).update(updateUserDto());
    }

    @Test
    @DisplayName("Должен удалить пользователя")
    void should_remove_user() throws Exception {
        doNothing().when(userService).remove(eq(TEST_USER_ID));

        mockMvc.perform(delete("/users/{userId}", TEST_USER_ID))
                .andExpect(status().isOk())
                .andDo(print());

        verify(userService, times(1)).remove(TEST_USER_ID);
    }
}