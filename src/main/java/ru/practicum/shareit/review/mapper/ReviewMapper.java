package ru.practicum.shareit.review.mapper;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.review.dto.ReviewDto;
import ru.practicum.shareit.review.model.Review;
import ru.practicum.shareit.review.dto.ReviewResponse;
import ru.practicum.shareit.review.dto.ReviewWithUserIdResponse;
import ru.practicum.shareit.review.dto.UpdateReviewRequest;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ReviewMapper {

    public static Review mapToReview(ReviewDto review) {

        return Review.builder()
                .content(review.getContent())
                .isComplete(review.getIsComplete())
                .isPositive(review.getIsPositive())
                .useful("0.0")
                .itemId(review.getItemId())
                .consumerId(review.getConsumerId())
                .ownerId(review.getOwnerId())
                .build();
    }

    public static ReviewResponse mapToReviewResponse(Review review) {
        return new ReviewResponse(
                review.getId(),
                review.getContent(),
                review.getIsComplete(),
                review.getIsPositive(),
                review.getUseful());
    }

    public static ReviewWithUserIdResponse mapToReviewWithUserIdDtoDto(Review review) {
        return new ReviewWithUserIdResponse(
                review.getId(),
                review.getContent(),
                review.getIsComplete(),
                review.getIsPositive(),
                review.getUseful(),
                review.getConsumerId()
                );
    }

    public static Review updateReviewFields(Review review, UpdateReviewRequest request) {
        if (request.hasContent()) {
            review.setContent(request.getContent());
        }
        if (request.hasIsComplete()) {
            review.setIsComplete(request.getIsComplete());
        }
        if (request.hasIsPositive()) {
            review.setIsPositive(request.getIsPositive());
        }

        return review;
    }
}
