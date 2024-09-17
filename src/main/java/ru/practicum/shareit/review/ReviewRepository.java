package ru.practicum.shareit.review;

import ru.practicum.shareit.review.model.Review;

import java.util.Collection;
import java.util.Optional;

public interface ReviewRepository {

    Optional<Review> create(Review review);

    Optional<Review> update(Review review);

    boolean delete(Long id);

    Optional<Review> getById(long id);

    Collection<Review> getReviews(Long userId, Long itemId, Integer count);

    void addScore(long id, int score);
}
