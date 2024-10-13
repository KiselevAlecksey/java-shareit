package ru.practicum.shareit.item;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.BookingAvailable;
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
import java.time.format.DateTimeFormatter;
import java.util.*;

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
    public List<ItemResponseDto> getAll(long ownerId) {
        List<Item> items = itemRepository.findAllByOwnerId(ownerId);
        List<Long> itemIds = items.stream().map(Item::getId).toList();

        List<Booking> bookingList = bookingRepository
                .findByItemIdAndEndBookingBeforeAndStartBookingAfterAndStatus(
                        itemIds, LocalDateTime.now());

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

        List<ItemResponseDto> itemsDto = items.stream()
                .map(itemMapper::mapToItemDto)
                .toList();

        setAdjacentBooking(itemsDto, bookingMap);

        return itemsDto;
    }

    private void setAdjacentBooking(List<ItemResponseDto> itemsDto, Map<Long, List<Booking>> bookingMap) {
        for (ItemResponseDto itemResponseDto : itemsDto) {

            List<Booking> nextAndLastList = bookingMap.get(itemResponseDto.getId());

            if (nextAndLastList == null) {
                continue;
            }

            LocalDateTime firstEnd = nextAndLastList.getFirst().getEndBooking();
            LocalDateTime firstStart = nextAndLastList.getFirst().getStartBooking();
            LocalDateTime lastEnd = nextAndLastList.getLast().getEndBooking();

            if (nextAndLastList.size() == 1) {

                if (firstEnd.isBefore(LocalDateTime.now())) {
                    itemResponseDto.setLastBooking(convertDateFormat(firstEnd));
                } else {
                    itemResponseDto.setNextBooking(convertDateFormat(firstStart));
                }
            } else {
                if (firstEnd.isAfter(lastEnd)) {
                    itemResponseDto.setNextBooking(convertDateFormat(firstStart));
                    itemResponseDto.setLastBooking(convertDateFormat(lastEnd));
                } else {
                    itemResponseDto.setNextBooking(convertDateFormat(nextAndLastList.getLast().getStartBooking()));
                    itemResponseDto.setLastBooking(convertDateFormat(firstEnd));
                }
            }
        }
    }

    private String convertDateFormat(LocalDateTime localDateTime) {
        String dateTimeformatted = null;

        if (localDateTime != null) {
            dateTimeformatted = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")
                    .withZone(ZoneOffset.UTC)
                    .format(localDateTime);
        }
        return dateTimeformatted;
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

        Item item = itemRepository.findByIdAndOwnerId(itemDto.getId(), itemDto.getOwnerId())
                .orElseThrow(() -> new NotFoundException("Предмет не найден"));

        itemMapper.updateItemFields(item, itemDto);

        Item itemUpdated = itemRepository.save(item);

        return itemMapper.mapToItemDto(itemUpdated);
    }

    @Override
    public void delete(long itemId) {
        itemRepository.deleteById(itemId);
    }

    @Override
    public ItemResponseDto get(long userId, long itemId) {

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
    public CommentResponseDto createComment(CommentCreateDto comment) {
        User user = userRepository.findById(comment.getUserId())
                .orElseThrow(() -> new NotFoundException("Пользователь не найден"));

        Item item = itemRepository.findById(comment.getItemId())
                .orElseThrow(() -> new NotFoundException("Предмет не найден"));

        bookingRepository.findByItemIdAndBookerIdAndEndBookingBefore(comment.getItemId(),
                        comment.getUserId(), LocalDateTime.now(), Sort.by(Sort.Direction.DESC, "endBooking"))
                .orElseThrow(() -> new ParameterNotValidException("Пользователь не пользовался предметом",
                        comment.getUserId().toString()));

        Comment newComment = commentMapper.mapToComment(comment);

        newComment.setItem(item);

        newComment.setAuthor(user);

        Comment savedComment = commentRepository.save(newComment);

        return commentMapper.mapToCommentDto(savedComment);
    }
}
