package ru.practicum.shareit.review;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.review.dto.ReviewDto;
import ru.practicum.shareit.review.dto.ReviewResponse;
import ru.practicum.shareit.review.dto.UpdateReviewRequest;
import ru.practicum.shareit.review.mapper.ReviewMapper;
import ru.practicum.shareit.review.model.Review;
import ru.practicum.shareit.validator.Validator;

import java.util.Collection;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ReviewServiceImpl implements ReviewService {
    final ReviewRepository reviewRepository;

    final Validator validator;

    @Override
    public ReviewResponse create(ReviewDto reviewRequest) {

        validator.validateReviewRequest(reviewRequest);

        Review review = ReviewMapper.mapToReview(reviewRequest);

        Review savedReview = reviewRepository.create(review).orElseThrow(
                () -> new NotFoundException("Отзыв не найден")
        );

        return ReviewMapper.mapToReviewResponse(savedReview);
    }

    @Override
    public ReviewResponse update(UpdateReviewRequest reviewRequest, long id) {

        Review review = reviewRepository.getById(id)
                .orElseThrow(() -> new NotFoundException("Отзыв не найден"));

        ReviewMapper.updateReviewFields(review, reviewRequest);

        Review reviewUpdated = reviewRepository.update(review).orElseThrow(
                () -> new NotFoundException("Отзыв не найден")
        );

        return ReviewMapper.mapToReviewResponse(reviewUpdated);
    }

    @Override
    public boolean delete(Long id) {
        if (id == null) {
            throw new NotFoundException("Отзыв не найден");
        }

        reviewRepository.getById(id).orElseThrow(
                () -> new NotFoundException("Отзыв не найден")
        );

        return reviewRepository.delete(id);
    }

    @Override
    public ReviewResponse getById(long id) {

        Review review = reviewRepository.getById(id).orElseThrow(
                () -> new NotFoundException("Отзыв не найден")
        );

        return ReviewMapper.mapToReviewResponse(review);
    }

    @Override
    public Collection<ReviewResponse> getReviews(Long userId, Long itemId, Integer count) {
        return reviewRepository.getReviews(userId, itemId, count).stream()
                .map(ReviewMapper::mapToReviewResponse)
                .toList();
    }

    @Override
    public void addScore(Long id, Integer score) {

        Review review = reviewRepository.getById(id)
                .orElseThrow(() -> new NotFoundException("Отзыв не найден"));

        reviewRepository.addScore(id, score);
    }
}
