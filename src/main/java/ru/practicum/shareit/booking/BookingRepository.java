package ru.practicum.shareit.booking;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.booking.model.Booking;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface BookingRepository extends JpaRepository<Booking, Long> {

    @Query("SELECT b FROM Booking b WHERE b.consumer.id = :id ORDER BY b.startBooking DESC")
    List<Booking> findAllByConsumerId(long id);

    @Query("SELECT b FROM Booking b WHERE b.consumer.id = :id AND b.status = 'APPROVED' " +
            "AND b.startBooking <= CURRENT_TIMESTAMP AND b.endBooking >= CURRENT_TIMESTAMP ORDER BY b.startBooking DESC")
    List<Booking> findCurrentByConsumerId(long id);

    @Query("SELECT b FROM Booking b WHERE b.consumer.id = :id AND b.status = 'APPROVED' " +
            "AND b.endBooking < CURRENT_TIMESTAMP ORDER BY b.startBooking DESC")
    List<Booking> findPastByConsumerId(long id);

    @Query("SELECT b FROM Booking b WHERE b.consumer.id = :id AND b.status = 'APPROVED' " +
            "AND b.startBooking > CURRENT_TIMESTAMP ORDER BY b.startBooking DESC")
    List<Booking> findFutureByConsumerId(long id);

    @Query("SELECT b FROM Booking b WHERE b.consumer.id = :id AND b.status = 'WAITING' ORDER BY b.startBooking DESC")
    List<Booking> findWaitingByConsumerId(long id);

    @Query("SELECT b FROM Booking b WHERE b.consumer.id = :id AND b.status = 'REJECTED' ORDER BY b.startBooking DESC")
    List<Booking> findRejectedByConsumerId(long id);

    @Query("SELECT b FROM Booking b WHERE b.owner.id = :id ORDER BY b.startBooking DESC")
    List<Booking> findAllByOwnerId(long id);

    @Query("SELECT b FROM Booking b WHERE b.owner.id = :id AND b.status = 'APPROVED' " +
            "AND b.startBooking <= CURRENT_TIMESTAMP AND b.endBooking >= CURRENT_TIMESTAMP ORDER BY b.startBooking DESC")
    List<Booking> findCurrentByOwnerId(long id);

    @Query("SELECT b FROM Booking b WHERE b.owner.id = :id AND b.status = 'APPROVED' " +
            "AND b.endBooking < CURRENT_TIMESTAMP ORDER BY b.startBooking DESC")
    List<Booking> findPastByOwnerId(long id);

    @Query("SELECT b FROM Booking b WHERE b.owner.id = :id AND b.status = 'APPROVED' " +
            "AND b.startBooking > CURRENT_TIMESTAMP ORDER BY b.startBooking DESC")
    List<Booking> findFutureByOwnerId(long id);

    @Query("SELECT b FROM Booking b WHERE b.owner.id = :id AND b.status = 'WAITING' ORDER BY b.startBooking DESC")
    List<Booking> findWaitingByOwnerId(long id);

    @Query("SELECT b FROM Booking b WHERE b.owner.id = :id AND b.status = 'REJECTED' ORDER BY b.startBooking DESC")
    List<Booking> findRejectedByOwnerId(long id);

    @EntityGraph(value = "booking.full", type = EntityGraph.EntityGraphType.FETCH)
    List<Booking> findByConsumerId(long consumerId);

    @Query(value = "SELECT * FROM Bookings b WHERE b.consumer_id = :consumerId AND b.item_id = :itemId " +
            "AND b.end_booking < :instant " +
            "ORDER BY b.end_booking DESC LIMIT 1", nativeQuery = true)
    Optional<Booking> findByItemIdAndConsumerIdAndEndBookingBefore(Long itemId, Long consumerId, LocalDateTime instant);

}