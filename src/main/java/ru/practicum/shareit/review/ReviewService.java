package ru.practicum.shareit.review;

import ru.practicum.shareit.review.dto.ReviewDto;
import ru.practicum.shareit.review.dto.ReviewResponse;
import ru.practicum.shareit.review.dto.UpdateReviewRequest;

import java.util.Collection;

public interface ReviewService {
    ReviewResponse create(ReviewDto review);

    ReviewResponse update(UpdateReviewRequest review, long id);

    boolean delete(Long id);

    ReviewResponse getById(long id);

    Collection<ReviewResponse> getReviews(Long userId, Long itemId, Integer count);

    void addScore(Long id, Integer score);
}
