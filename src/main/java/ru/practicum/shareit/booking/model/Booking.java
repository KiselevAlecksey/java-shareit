package ru.practicum.shareit.booking.model;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import ru.practicum.shareit.booking.BookingStatus;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

import static ru.practicum.shareit.util.Const.ONE_DAY_IN_MILLIS;

@Entity
@Table(name = "bookings")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id", nullable = false)
    User owner;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "item_id", nullable = false)
    Item item;

    @Column(name = "available", nullable = false)
    Boolean available;

    @Column(name = "start_booking", nullable = false)
    @Builder.Default
    LocalDateTime startBooking = LocalDateTime.ofInstant(Instant.EPOCH, ZoneId.systemDefault());

    @Column(name = "end_booking", nullable = false)
    @Builder.Default
    LocalDateTime endBooking = LocalDateTime.ofInstant(Instant.EPOCH
            .plusMillis(ONE_DAY_IN_MILLIS), ZoneId.systemDefault());

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "booker_id", nullable = false)
    User booker;

    @Column(name = "confirm_time", nullable = false)
    @Builder.Default
    LocalDateTime confirmTime = LocalDateTime.ofInstant(Instant.EPOCH, ZoneId.systemDefault());

    @Column(name = "content")
    String content;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    @Builder.Default
    BookingStatus status = BookingStatus.WAITING;
}
