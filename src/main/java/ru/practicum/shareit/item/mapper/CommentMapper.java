package ru.practicum.shareit.item.mapper;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.item.dto.*;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.time.Instant;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

@Component
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class CommentMapper {

    public Comment mapToComment(CommentCreateDto request) {

        return Comment.builder()
                .author(User.builder().id(request.getUserId()).build())
                //.item(Item.builder().id(request.getItemId()).build())
                .content(request.getText())
                .createdDate(Instant.now())
                .build();
    }

    public CommentResponseDto mapToCommentDto(Comment comment) {

        String created = "";

        if (comment.getCreatedDate() != null) {
            created = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")
                    .withZone(ZoneOffset.UTC)
                    .format(comment.getCreatedDate());
        }

        return new CommentResponseDto(
                comment.getId(),
                comment.getAuthor().getName(),
                comment.getContent(),
                created
        );
    }
}
