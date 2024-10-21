package ru.practicum.shareit.item.mapper;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.practicum.shareit.item.dto.CommentCreateDto;
import ru.practicum.shareit.item.dto.CommentResponseDto;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.user.model.User;

import static ru.practicum.shareit.util.TestDataFactory.*;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

@DisplayName("CommentMapper")
public class CommentMapperTest {

    private final CommentMapper commentMapper = new CommentMapper();

    @Test
    @DisplayName("Должен маппить дто в комментарий")
    public void should_map_to_comment() {
        CommentCreateDto dto = new CommentCreateDto("This is a comment", TEST_USER_ID, TEST_ITEM_ID);

        Comment comment = commentMapper.mapToComment(dto);

        assertEquals(TEST_USER_ID, comment.getAuthor().getId());
        assertEquals("This is a comment", comment.getContent());
        assertNotNull(comment.getCreated());
    }

    @Test
    @DisplayName("Должен маппить комментарий в дто")
    public void should_map_to_comment_dto() {
        User author = createUser();
        author.setName("Author Name");
        Comment comment = new Comment(TEST_ID_ONE, "This is a comment", itemCreate(), author, LocalDateTime.now());

        CommentResponseDto dto = commentMapper.mapToCommentDto(comment);

        assertEquals(TEST_ID_ONE, dto.id());
        assertEquals("Author Name", dto.authorName());
        assertEquals("This is a comment", dto.text());
        assertNotNull(dto.created());

        String expectedDate = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")
                .withZone(ZoneOffset.UTC)
                .format(comment.getCreated());
        assertEquals(expectedDate, dto.created());
    }

    @Test
    @DisplayName("Должен возвращать пустую строку для даты, если она равна null")
    public void should_return_empty_string_for_null_created_date() {
        User author = createUser();
        author.setName("Author Name");
        Comment comment = new Comment(TEST_ID_ONE, "This is a comment", itemCreate(), author, null);

        CommentResponseDto dto = commentMapper.mapToCommentDto(comment);

        assertEquals(TEST_ID_ONE, dto.id());
        assertEquals("Author Name", dto.authorName());
        assertEquals("This is a comment", dto.text());
        assertEquals("", dto.created());
    }
}