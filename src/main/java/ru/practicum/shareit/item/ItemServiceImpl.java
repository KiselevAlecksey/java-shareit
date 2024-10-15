package ru.practicum.shareit.item;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.BookingAvailable;
import ru.practicum.shareit.booking.BookingId;
import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.ParameterNotValidException;
import ru.practicum.shareit.item.dto.*;
import ru.practicum.shareit.item.mapper.CommentMapper;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.CommentRepository;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.model.User;

import java.time.*;
import java.util.*;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ItemServiceImpl implements ItemService {
    final ItemRepository itemRepository;

    final UserRepository userRepository;

    final CommentRepository commentRepository;

    final BookingRepository bookingRepository;

    final ItemMapper itemMapper;

    final CommentMapper commentMapper;

    @Override
    public List<ItemResponseDto> getAll(long ownerId) {
        List<Item> items = itemRepository.findAllByOwnerId(ownerId);
        List<Long> itemIds = items.stream().map(Item::getId).toList();
        List<Booking> bookingList = bookingRepository
                .findByItemIdAndEndBookingBeforeAndStartBookingAfterAndStatus(itemIds, LocalDateTime.now());
        List<ItemResponseDto> itemResponseDtos = new ArrayList<>(items.size());

        if (bookingList.isEmpty()) {
            return items.stream()
                    .map(itemMapper::mapToItemDto)
                    .toList();
        }

        Map<Long, List<Booking>> bookingMap = new HashMap<>(items.size());

        for (Booking booking : bookingList) {
            long itemId = booking.getItem().getId();

            if (!bookingMap.containsKey(itemId)) {
                bookingMap.put(itemId, new ArrayList<>());
            }

            List<Booking> itemBookings = bookingMap.get(itemId);
            itemBookings.add(booking);
        }

        for (Item item : items) {

            List<Booking> nextAndLastListUnsorted = bookingMap.get(item.getId());

            if (nextAndLastListUnsorted == null) {
                continue;
            }

            itemResponseDtos.add(itemMapper.mapToItemDto(
                    item,
                    getFirstAndLastBooking(nextAndLastListUnsorted).getLast(),
                    getFirstAndLastBooking(nextAndLastListUnsorted).getFirst())
            );
        }

        return itemResponseDtos;
    }

    @Override
    @Transactional(isolation = Isolation.SERIALIZABLE)
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
    @Transactional(isolation = Isolation.SERIALIZABLE)
    public ItemResponseDto update(ItemUpdateDto itemDto) {

        Item item = itemRepository.findByIdAndOwnerId(itemDto.getId(), itemDto.getOwnerId())
                .orElseThrow(() -> new NotFoundException("Предмет не найден"));

        itemMapper.updateItemFields(item, itemDto);

        Item itemUpdated = itemRepository.save(item);

        return itemMapper.mapToItemDto(itemUpdated);
    }

    @Override
    @Transactional
    public void delete(long itemId) {
        itemRepository.deleteById(itemId);
    }

    @Override
    public ItemResponseDto get(long itemId) {

        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new NotFoundException("Предмет не найден"));

        Optional<BookingAvailable> optionalBookingAvailable = bookingRepository
                .findByItemIdAndEndBookingAfter(itemId, LocalDateTime.now());

        if (optionalBookingAvailable.isPresent()) {
            item.setAvailable(false);
        }

        ItemResponseDto itemResponseDto = itemMapper.mapToItemDto(item);

        List<CommentResponseDto> commentResponseDtos = commentRepository.findAllByItemId(itemId).stream()
                        .map(commentMapper::mapToCommentDto)
                                .toList();

        itemResponseDto.setComments(commentResponseDtos);

        return itemResponseDto;
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
    @Transactional(isolation = Isolation.READ_UNCOMMITTED)
    public CommentResponseDto createComment(CommentCreateDto comment) {
        User user = userRepository.findById(comment.getUserId())
                .orElseThrow(() -> new NotFoundException("Пользователь не найден"));

        Item item = itemRepository.findById(comment.getItemId())
                .orElseThrow(() -> new NotFoundException("Предмет не найден"));

        BookingId bookingIdOnly = bookingRepository.findByItemIdAndBookerIdAndEndBookingLessThanEqual(comment.getItemId(),
                comment.getUserId(), LocalDateTime.now())
                .orElseThrow(() -> new ParameterNotValidException("Пользователь не пользовался предметом",
                        comment.getUserId().toString()));

        Comment newComment = commentMapper.mapToComment(comment);

        newComment.setItem(item);
        newComment.setAuthor(user);

        Comment savedComment = commentRepository.save(newComment);

        return commentMapper.mapToCommentDto(savedComment);
    }


    private List<LocalDateTime> getFirstAndLastBooking(List<Booking> nextAndLastListUnsorted) {
        int listSize = 2;
        LocalDateTime lastBooking = null;
        LocalDateTime nextBooking = null;
        List<LocalDateTime> nextAndLastList = new ArrayList<>(listSize);

        LocalDateTime firstEnd = nextAndLastListUnsorted.getFirst().getEndBooking();
        LocalDateTime firstStart = nextAndLastListUnsorted.getFirst().getStartBooking();
        LocalDateTime lastEnd = nextAndLastListUnsorted.getLast().getEndBooking();

        if (nextAndLastListUnsorted.size() == 1) {

            if (firstEnd.isBefore(LocalDateTime.now())) {
                lastBooking = firstEnd;
            } else {
                nextBooking = firstStart;
            }
        } else {
            if (firstEnd.isAfter(lastEnd)) {
                nextBooking = firstStart;
                lastBooking = lastEnd;
            } else {
                nextBooking = nextAndLastListUnsorted.getLast().getStartBooking();
                lastBooking = firstEnd;
            }
        }

        nextAndLastList.add(nextBooking);
        nextAndLastList.add(lastBooking);
        return nextAndLastList;
    }
}
