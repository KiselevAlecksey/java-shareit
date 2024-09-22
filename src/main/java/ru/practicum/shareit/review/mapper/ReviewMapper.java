package ru.practicum.shareit.review.mapper;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.review.model.Review;
import ru.practicum.shareit.review.dto.NewReviewRequest;
import ru.practicum.shareit.review.dto.ReviewResponse;
import ru.practicum.shareit.review.dto.ReviewWithUserIdResponse;
import ru.practicum.shareit.review.dto.UpdateReviewRequest;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ReviewMapper {

    public static Review mapToReview(NewReviewRequest review) {

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

    public static Review mapToReview(ReviewResponse review) {

        return Review.builder()
                .id(review.getId())
                .content(review.getContent())
                .isComplete(review.getIsComplete())
                .isPositive(review.getIsPositive())
                .useful(review.getUseful())
                .build();
    }

    public static ReviewResponse mapToReviewResponse(Review review) {
        ReviewResponse dto = new ReviewResponse();
        dto.setId(review.getId());
        dto.setContent(review.getContent());
        dto.setIsComplete(review.getIsComplete());
        dto.setIsPositive(review.getIsPositive());
        dto.setUseful(review.getUseful());

        return dto;
    }

    public static ReviewWithUserIdResponse mapToReviewWithUserIdDtoDto(Review review) {
        ReviewWithUserIdResponse dto = new ReviewWithUserIdResponse();
        dto.setId(review.getId());
        dto.setContent(review.getContent());
        dto.setIsComplete(review.getIsComplete());
        dto.setIsPositive(review.getIsPositive());
        dto.setConsumerId(review.getConsumerId());
        dto.setUseful(review.getUseful());

        return dto;
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
