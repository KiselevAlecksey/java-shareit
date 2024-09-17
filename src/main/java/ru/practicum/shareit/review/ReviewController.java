package ru.practicum.shareit.review;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.review.dto.NewReviewRequest;
import ru.practicum.shareit.review.dto.ReviewResponse;
import ru.practicum.shareit.review.dto.UpdateReviewRequest;

import java.util.Collection;

@Slf4j
@Validated
@RestController
@RequestMapping("/reviews")
@RequiredArgsConstructor
public class ReviewController {
    private final ReviewService reviewService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ReviewResponse create(@RequestBody NewReviewRequest reviewDto) {
        log.error("Create review  {} start", reviewDto);
        ReviewResponse created = reviewService.create(reviewDto);
        log.error("Created review is {} complete", created.getId());
        return created;
    }

    @PatchMapping
    public ReviewResponse update(@RequestBody UpdateReviewRequest reviewRequest) {
        log.error("Update review {} start", reviewRequest);
        ReviewResponse updated = reviewService.update(reviewRequest);
        log.error("Updated review is {} complete", updated.getId());
        return updated;
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        log.error("Delete review id {} start", id);
        reviewService.delete(id);
        log.error("Deleted review id is {} complete", id);
    }

    @GetMapping("/{id}")
    public ReviewResponse getById(@PathVariable long id) {
        log.error("Get review id {} start", id);
        ReviewResponse reviewDto = reviewService.getById(id);
        log.error("Get review id is {} complete", id);
        return reviewDto;
    }

    @GetMapping
    public Collection<ReviewResponse> getAll(
            @RequestParam(required = false) Long userId,
            @RequestParam(required = false) Long itemId,
            @RequestParam(defaultValue = "1") @Positive Integer count) {

        log.error("Get all review by userId {}, itemId {}, count {} start", userId, itemId, count);
        Collection<ReviewResponse> reviewDto = reviewService.getReviews(userId, itemId, count);
        log.error("Get all review by userId {}, itemId {}, count {} complete", userId, itemId, count);
        return reviewDto;
    }

    @PatchMapping("/{id}/{score}")
    public void addScore(@PathVariable long id, @PathVariable @Min(1) @Max(5) int score) {
        log.error("Add score review id {}, score {} start", id, score);
        reviewService.addScore(id, score);
        log.error("Added score review id {}, score {} complete", id, score);
    }
}
