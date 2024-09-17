package ru.practicum.shareit.review;

import ru.practicum.shareit.review.dto.NewReviewRequest;
import ru.practicum.shareit.review.dto.ReviewResponse;
import ru.practicum.shareit.review.dto.UpdateReviewRequest;

import java.util.Collection;

public interface ReviewService {
    ReviewResponse create(NewReviewRequest review);

    ReviewResponse update(UpdateReviewRequest review);

    boolean delete(Long id);

    ReviewResponse getById(long id);

    Collection<ReviewResponse> getReviews(Long userId, Long itemId, Integer count);

    void addScore(Long id, Integer score);
}
