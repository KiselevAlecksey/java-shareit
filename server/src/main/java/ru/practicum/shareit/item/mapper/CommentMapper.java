package ru.practicum.shareit.item.mapper;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.item.dto.*;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

@Component
public final class CommentMapper {

    public Comment mapToComment(CommentCreateDto request) {

        return Comment.builder()
                .author(User.builder().id(request.getUserId()).build())
                .content(request.getText())
                .created(LocalDateTime.now())
                .build();
    }

    public CommentResponseDto mapToCommentDto(Comment comment) {

        String created = "";

        if (comment.getCreated() != null) {
            created = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")
                    .withZone(ZoneOffset.UTC)
                    .format(comment.getCreated());
        }

        return new CommentResponseDto(
                comment.getId(),
                comment.getAuthor().getName(),
                comment.getContent(),
                created
        );
    }
}
