package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.ItemShort;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.request.dto.ItemRequestCreateDto;
import ru.practicum.shareit.request.dto.ItemRequestResponseDto;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.UserRepository;

import java.util.*;

@Service
@RequiredArgsConstructor
public class ItemRequestServiceImpl implements ItemRequestService {

    static final Sort SORT_BY_CREATE_DATE_DESC = Sort.by(Sort.Direction.DESC, "created");

    final ItemRequestMapper itemRequestMapper;

    final ItemMapper itemMapper;

    final UserRepository userRepository;

    final ItemRequestRepository itemRequestRepository;

    final ItemRepository itemRepository;

    @Override
    public List<ItemRequestResponseDto> getAllByUserId(long requestorId) {

        List<ItemRequest> itemRequests = itemRequestRepository.findAllByRequestorId(requestorId);

        List<Long> itemRequestIds = itemRequests.stream().map(ItemRequest::getId).toList();
        List<ItemShort> itemShortList = itemRepository.findByItemRequestIds(itemRequestIds);

        Map<Long, List<ItemShort>> itemShortMap = new HashMap<>(itemShortList.size());

        List<ItemRequestResponseDto> responseDtoList = new ArrayList<>(itemRequestIds.size());

        if (itemShortList.isEmpty()) {
            return itemRequests.stream()
                    .map(itemRequestMapper::mapToItemRequestDto)
                    .toList();
        }

        for (ItemShort itemShort : itemShortList) {
            long itemId = itemShort.getId();
            itemShortMap.computeIfAbsent(itemId, k -> new ArrayList<>()).add(itemShort);
        }

        for (ItemRequest itemRequest : itemRequests) {
            long itemId = itemRequest.getItem().getId();
            ItemRequestResponseDto responseDto = itemRequestMapper
                    .mapToItemRequestDto(itemRequest, itemShortMap.get(itemId));
            responseDtoList.add(responseDto);
        }

        return responseDtoList;
    }

    @Override
    public List<ItemRequestResponseDto> getAll() {
        List<ItemRequest> itemRequests = itemRequestRepository.findAll(SORT_BY_CREATE_DATE_DESC);
        List<Long> itemRequestIds = itemRequests.stream().map(ItemRequest::getId).toList();
        List<ItemShort> itemShortList = itemRepository.findByItemRequestIds(itemRequestIds);

        Map<Long, List<ItemShort>> itemShortMap = new HashMap<>(itemShortList.size());

        List<ItemRequestResponseDto> responseDtoList = new ArrayList<>(itemRequestIds.size());

        if (itemShortList.isEmpty()) {
            return itemRequests.stream()
                    .map(itemRequestMapper::mapToItemRequestDto)
                    .toList();
        }

        for (ItemShort itemShort : itemShortList) {
            long itemId = itemShort.getId();
            itemShortMap.computeIfAbsent(itemId, k -> new ArrayList<>()).add(itemShort);
        }

        for (ItemRequest itemRequest : itemRequests) {
            long itemId = itemRequest.getItem().getId();
            ItemRequestResponseDto responseDto = itemRequestMapper
                    .mapToItemRequestDto(itemRequest, itemShortMap.get(itemId));
            responseDtoList.add(responseDto);
        }

        return responseDtoList;
    }

    @Override
    public ItemRequestResponseDto create(ItemRequestCreateDto createDto) {

        ItemRequest itemRequest = itemRequestMapper.mapToItemRequest(createDto);

        ItemRequest itemRequestCreated = itemRequestRepository.save(itemRequest);

        return itemRequestMapper.mapToItemRequestDto(itemRequestCreated);
    }

    @Override
    public ItemRequestResponseDto get(long itemRequestId) {
        ItemRequest itemRequest = itemRequestRepository.findById(itemRequestId)
                .orElseThrow(() -> new NotFoundException("Запрос не найден"));

        List<Long> listIds = Collections.singletonList(itemRequestId);

        List<ItemShort> itemShortList = itemRepository
                .findByItemRequestIds(listIds);

        return itemRequestMapper.mapToItemRequestDto(itemRequest, itemShortList);
    }

}
