package ru.practicum.shareit.item.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.item.ItemShort;
import ru.practicum.shareit.item.model.Item;

import java.util.List;
import java.util.Optional;

public interface ItemRepository extends JpaRepository<Item, Long> {

    Optional<Item> findByIdAndOwnerId(Long itemId, Long ownerId);

    List<Item> findByNameContainingIgnoreCase(String text);

    List<Item> findAllByOwnerId(Long ownerId);

    @Query("SELECT i.id AS id, i.name AS name, i.owner.id AS ownerId " +
            "FROM Item i " +
            "WHERE i.requestId IN (:itemRequestIds)")
    List<ItemShort> findByItemRequestIds(List<Long> itemRequestIds);
}
