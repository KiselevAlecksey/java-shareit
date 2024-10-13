package ru.practicum.shareit.item.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import ru.practicum.shareit.item.model.Item;

import java.util.List;
import java.util.Optional;

public interface ItemRepository extends JpaRepository<Item, Long> {

    @Override
    @EntityGraph("item.owner")
    Optional<Item> findById(Long itemId);

    @EntityGraph("item.owner")
    Optional<Item> findByIdAndOwnerId(Long itemId, Long ownerId);

    List<Item> findByNameContainingIgnoreCase(String text);

    @EntityGraph("item.owner")
    List<Item> findAllByOwnerId(@Param("ownerId") Long ownerId);

    @EntityGraph("item.owner")
    Integer countItemsByOwnerId(@Param("ownerId") Long ownerId);
}
