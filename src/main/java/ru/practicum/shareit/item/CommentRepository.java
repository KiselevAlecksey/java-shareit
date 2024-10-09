package ru.practicum.shareit.item;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.practicum.shareit.item.model.Comment;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    @EntityGraph(value = "comment.full", type = EntityGraph.EntityGraphType.FETCH)
    @Query("SELECT c FROM Comment c WHERE c.item.id = :id")
    List<Comment> findAllByItemId(@Param("id") long id);
}
