package ru.practicum.shareit.item;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.ParameterNotValidException;
import ru.practicum.shareit.item.dto.*;
import ru.practicum.shareit.item.mapper.CommentMapper;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.model.User;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ItemServiceImpl implements ItemService {
    final ItemRepository itemRepository;

    final UserRepository userRepository;

    final CommentRepository commentRepository;

    final BookingRepository bookingRepository;

    final ItemMapper itemMapper;

    final CommentMapper commentMapper;

    @Override
    public List<ItemResponseDto> getAll(long userId) {
        List<Item> items = itemRepository.findByUserId(userId);
        List<Long> ids = items.stream().map(Item::getId).toList();
        List<Booking> bookingList = bookingRepository.findByConsumerId(userId);

        if (bookingList.isEmpty()) {
            return items.stream()
                    .map(itemMapper::mapToItemDto)
                    .toList();
        }

        Map<Long, List<LocalDateTime>> bookingMap = new HashMap<>();

        for (Long id : ids) {
            List<Booking> list = new ArrayList<>();
            for (Booking booking : bookingList) {
                if (booking.getItem().getId().equals(id)) {
                    list.add(booking);
                }
            }

            List<Booking> sortedLastBookings = list.stream()
                    .filter(booking -> booking.getEndBooking()
                            .isBefore(LocalDateTime.now()))
                    .sorted(Comparator.comparing(Booking::getEndBooking).reversed())
                    .toList();

            List<Booking> sortedNextBookings = list.stream()
                    .filter(booking -> booking.getStartBooking()
                            .isAfter(LocalDateTime.now()))
                    .sorted(Comparator.comparing(Booking::getStartBooking))
                    .toList();

            List<LocalDateTime> nextAndLastBookingList = new ArrayList<>();

            nextAndLastBookingList.add(sortedNextBookings.getFirst().getStartBooking());
            nextAndLastBookingList.add(sortedLastBookings.getFirst().getEndBooking());

            bookingMap.put(id, nextAndLastBookingList);

        }

        for (Item item : items) {
            List<LocalDateTime> nextAndLastList = bookingMap.get(item.getId());
            item.setNextBooking(nextAndLastList.getFirst());
            item.setLastBooking(nextAndLastList.getLast());
        }

        return items.stream()
                .map(itemMapper::mapToItemDto)
                .toList();
    }

    @Override
    public ItemResponseDto create(ItemCreateDto itemDto) {

        Item item = itemMapper.mapToItem(itemDto);

        User user = userRepository.findById(item.getOwner().getId())
                .orElseThrow(() -> new NotFoundException("Пользователь не найден"));

        item.getOwner().setEmail(user.getEmail());
        item.getOwner().setName(user.getName());

        Item itemCreated = itemRepository.save(item);

        return itemMapper.mapToItemDto(itemCreated);
    }

    @Override
    public ItemResponseDto update(ItemUpdateDto itemDto) {

        Item item = itemRepository.findById(itemDto.getId())
                .orElseThrow(() -> new NotFoundException("Предмет не найден"));

        User user = item.getOwner();

        if (user == null || !(user.getId() != null && user.getId().equals(itemDto.getUserId()))) {
            throw new NotFoundException("Id заголовка и владельца не совпадают");
        }

        itemMapper.updateItemFields(item, itemDto);

        Item itemUpdated = itemRepository.save(item);

        return itemMapper.mapToItemDto(itemUpdated);
    }

    @Override
    public void delete(long itemId) {

        if (itemRepository.findById(itemId).isEmpty()) {
            throw new NotFoundException("Предмет не найден");
        }

        itemRepository.deleteById(itemId);
    }

    @Override
    public ItemResponseDto get(long userId, long itemId) {

        if (userRepository.findById(userId).isEmpty()) {
            throw new NotFoundException("Пользователь не найден");
        }

        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new NotFoundException("Предмет не найден"));

        item.setComments(commentRepository.findAllByItemId(itemId));

        return itemMapper.mapToItemDto(item);
    }

    @Override
    public List<ItemResponseDto> search(String text) {

        if (text.isEmpty()) return Collections.emptyList();

        return itemRepository.findByNameContainingIgnoreCase(text).stream()
                .filter(Item::getAvailable)
                .map(itemMapper::mapToItemDto)
                .toList();
    }

    @Override
    public CommentResponseDto createComment(CommentCreateDto comment) {
        User user = userRepository.findById(comment.getUserId())
                .orElseThrow(() -> new NotFoundException("Пользователь не найден"));

        Item item = itemRepository.findById(comment.getItemId())
                .orElseThrow(() -> new NotFoundException("Предмет не найден"));

        /*ZoneId zoneId = ZoneId.systemDefault();

        ZonedDateTime zonedUser = ZonedDateTime.now(zoneId);

        int offsetHours = zonedUser.getOffset().getTotalSeconds() / 3600;*/

        //log.info("==> offsetHours: " + offsetHours);
        Instant now = Instant.now();//.plusMillis(Duration.ofHours(offsetHours).toMillis());
        log.info("==> current_time with tz: " + now);
        String formattedInstant = DateTimeFormatter.ofPattern("MMM d, yyyy, hh:mm:ss a")
                .withZone(ZoneId.of("UTC"))
                .format(now);
        log.info("==> formattedInstant: " + formattedInstant);

        LocalDateTime dateTime = LocalDateTime.now();
        log.info("==> dateTime: " + dateTime);
        bookingRepository.findByItemIdAndConsumerIdAndEndBookingBefore(comment.getItemId(), comment.getUserId(), dateTime)
                .orElseThrow(() -> new ParameterNotValidException("Пользователь не пользовался предметом",
                        comment.getUserId().toString()));

        Comment newComment = commentMapper.mapToComment(comment);

        newComment.setItem(item);

        newComment.setConsumer(user);

        Comment savedComment = commentRepository.save(newComment);

        return commentMapper.mapToCommentDto(savedComment);
    }
}
