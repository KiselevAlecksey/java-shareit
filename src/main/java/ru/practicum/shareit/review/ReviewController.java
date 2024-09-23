package ru.practicum.shareit.review;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.review.dto.ReviewDto;
import ru.practicum.shareit.review.dto.ReviewResponse;
import ru.practicum.shareit.review.dto.UpdateReviewRequest;
import ru.practicum.shareit.util.Marker;

import java.util.Collection;

@Slf4j
@Validated
@RestController
@RequestMapping("/reviews")
@RequiredArgsConstructor
public class ReviewController {
    private final ReviewService reviewService;

    @PostMapping
    @Validated({Marker.OnCreate.class})
    @ResponseStatus(HttpStatus.CREATED)
    public ReviewResponse create(@RequestBody @Valid ReviewDto reviewDto) {
        ReviewResponse created = reviewService.create(reviewDto);
        log.info("Created review is {} complete", created.id());
        return created;
    }


    @PatchMapping("/{id}")
    @Validated({Marker.OnUpdate.class})
    public ReviewResponse update(@RequestBody @Valid UpdateReviewRequest reviewRequest, @PathVariable long id) {
        ReviewResponse updated = reviewService.update(reviewRequest, id);
        log.info("Updated review is {} complete", id);
        return updated;
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        reviewService.delete(id);
        log.info("Deleted review id is {} complete", id);
    }

    @GetMapping("/{id}")
    public ReviewResponse getById(@PathVariable long id) {
        ReviewResponse reviewDto = reviewService.getById(id);
        log.info("Get review id is {} complete", id);
        return reviewDto;
    }

    @GetMapping
    public Collection<ReviewResponse> getAll(
            @RequestParam(required = false) Long userId,
            @RequestParam(required = false) Long itemId,
            @RequestParam(defaultValue = "1") @Positive Integer count) {

        Collection<ReviewResponse> reviewDto = reviewService.getReviews(userId, itemId, count);
        log.info("Get all review by userId {}, itemId {}, count {} complete", userId, itemId, count);
        return reviewDto;
    }

    @PatchMapping("/{id}/{score}")
    public void addScore(@PathVariable long id, @PathVariable @Min(1) @Max(5) int score) {
        reviewService.addScore(id, score);
        log.info("Added score review id {}, score {} complete", id, score);
    }
}
