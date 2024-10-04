package ru.practicum.shareit.item;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.practicum.shareit.item.model.Item;

import java.util.List;
import java.util.Optional;

public interface ItemRepository extends JpaRepository<Item, Long> {

    @Override
    @EntityGraph("item.owner")
    Optional<Item> findById(Long id);

    List<Item> findByNameContainingIgnoreCase(String text);

    @EntityGraph("item.owner")
    @Query("SELECT i FROM Item i WHERE i.owner.id = :userId")
    List<Item> findByUserId(@Param("userId") Long userId);

    @EntityGraph("item.owner")
    @Query("SELECT COUNT(i) FROM Item i WHERE i.owner.id = :userId")
    Integer findCountBookingsByUserId(@Param("userId") Long userId);

}
