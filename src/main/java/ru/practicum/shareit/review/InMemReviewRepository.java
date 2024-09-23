package ru.practicum.shareit.review;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.review.model.Review;

import java.util.*;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class InMemReviewRepository implements ReviewRepository {

    private long countId = 0;

    private final Map<Long, Review> reviewMap;

    private final Map<Long, List<Integer>> scoreMap;

    @Override
    public Optional<Review> create(Review review) {

        countId++;

        review.setId(countId);

        reviewMap.put(countId, review);

        scoreMap.put(countId, new ArrayList<>());

        return getReviewIfExist(countId);
    }

    @Override
    public Optional<Review> update(Review review) {

        Review reviewUpdated = calculateScore(review);

        reviewMap.put(review.getId(), reviewUpdated);

        return getReviewIfExist(reviewUpdated.getId());
    }

    @Override
    public boolean delete(Long id) {
        return reviewMap.remove(id) != null;
    }

    @Override
    public Optional<Review>  getById(long id) {
        return getReviewIfExist(id);
    }

    @Override
    public Collection<Review> getReviews(Long userId, Long itemId, Integer count) {

        Set<Review> reviewSet = new LinkedHashSet<>(count);

        if (userId != null && itemId != null) {

            reviewSet = reviewMap.values().stream()
                    .filter(review -> review.getConsumerId().equals(userId)
                            && review.getItemId().equals(itemId))
                    .collect(Collectors.toCollection(() -> new LinkedHashSet<>(count)));
        }

        if (userId == null) {

            reviewMap.values().stream()
                    .filter(review -> review.getItemId().equals(itemId))
                    .forEach(reviewSet::add);
        }

        if (itemId == null) {

            reviewMap.values().stream()
                    .filter(review -> review.getConsumerId().equals(userId))
                    .forEach(reviewSet::add);
        }

        List<Review> reviews = new ArrayList<>(reviewSet.stream()
                .toList());

        reviews.sort(Comparator.comparing(
                review -> Double.valueOf(((Review) review).getUseful())).reversed());

        return reviews.subList(0, Math.min(reviews.size(), count));
    }

    @Override
    public void addScore(long id, int score) {

        if (scoreMap.get(id) == null) {
            throw new NotFoundException("Отзыв не найден");
        }

        List<Integer> scoreList = scoreMap.get(id);

        scoreList.add(score);

        scoreMap.put(id, scoreList);

        calculateScore(reviewMap.get(id));
    }

    private Optional<Review> getReviewIfExist(Long reviewId) {
        Review review = reviewMap.get(reviewId);

        if (review == null) {
            throw new NotFoundException("Отзыв не найден");
        }

        return Optional.of(review);
    }

    private Review calculateScore(Review review) {
        Double score = scoreMap.get(review.getId()).stream()
                .mapToDouble(num -> num)
                .average()
                .orElse(0.0);

        return review.toBuilder()
                .useful(String.format("%.1f", score))
                .build();
    }
}
