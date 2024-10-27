package ru.practicum.shareit.booking;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingId;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface BookingRepository extends JpaRepository<Booking, Long> {

    List<Booking> findAllByBookerId(long bookerId, Sort sort);

    @Query("SELECT b FROM Booking b WHERE b.booker.id = :bookerId AND b.status = 'APPROVED' " +
           "AND b.startBooking <= CURRENT_TIMESTAMP AND b.endBooking >= CURRENT_TIMESTAMP")
    List<Booking> findAllCurrentByBookerIdAndStatusApproved(long bookerId, Sort sort);

    @Query("SELECT b FROM Booking b WHERE b.booker.id = :bookerId AND b.status = 'APPROVED' " +
            "AND b.endBooking < CURRENT_TIMESTAMP")
    List<Booking> findAllByBookerIdPast(long bookerId, Sort sort);

    @Query("SELECT b FROM Booking b WHERE b.booker.id = :bookerId AND b.status = 'APPROVED' " +
            "AND b.startBooking > CURRENT_TIMESTAMP")
    List<Booking> findFutureByBookerId(long bookerId, Sort sort);

    List<Booking> findAllCurrentByBookerIdAndStatus(long bookerId, BookingStatus status, Sort sort);

    List<Booking> findAllByOwnerId(long ownerId, Sort sort);

    @Query("SELECT b FROM Booking b WHERE b.owner.id = :ownerId AND b.status = 'APPROVED' " +
            "AND b.startBooking <= CURRENT_TIMESTAMP AND b.endBooking >= CURRENT_TIMESTAMP")
    List<Booking> findCurrentByOwnerId(long ownerId, Sort sort);

    @Query("SELECT b FROM Booking b WHERE b.owner.id = :ownerId AND b.status = 'APPROVED' " +
            "AND b.endBooking < CURRENT_TIMESTAMP")
    List<Booking> findPastByOwnerId(long ownerId, Sort sort);

    @Query("SELECT b FROM Booking b WHERE b.owner.id = :ownerId AND b.status = 'APPROVED' " +
            "AND b.startBooking > CURRENT_TIMESTAMP")
    List<Booking> findFutureByOwnerId(long ownerId, Sort sort);

    List<Booking> findAllCurrentByOwnerIdAndStatus(long ownerId, BookingStatus status, Sort sort);

    @Query(value = "SELECT next_booking.*\n" +
            "FROM (\n" +
            "    SELECT *\n" +
            "    FROM BOOKINGS b\n" +
            "    WHERE b.item_id IN (:itemIds) AND b.start_booking > :localDateTime AND b.status = 'APPROVED'\n" +
            "    ORDER BY b.start_booking ASC LIMIT 1\n" +
            ") AS next_booking\n" +
            "UNION ALL\n" +
            "SELECT last_booking.*\n" +
            "FROM (\n" +
            "    SELECT *\n" +
            "    FROM BOOKINGS b2\n" +
            "    WHERE b2.item_id IN (:itemIds) AND\n" +
            "    b2.end_booking < :localDateTime OR \n" +
            "    (b2.start_booking < :localDateTime AND b2.end_booking >= :localDateTime) AND b2.status = 'APPROVED'\n" +
            "    ORDER BY b2.end_booking DESC LIMIT 1\n" +
            ") AS last_booking",
            nativeQuery = true)
    List<Booking> findByItemIdAndEndBookingBeforeAndStartBookingAfterAndStatus(
            List<Long> itemIds, LocalDateTime localDateTime);

    Optional<BookingId> findByItemIdAndBookerIdAndEndBookingLessThanEqual(
            Long itemId, Long bookerId, LocalDateTime localDateTime);

    Optional<BookingAvailable> findByItemIdAndEndBookingAfter(long itemId, LocalDateTime localDateTime);
}