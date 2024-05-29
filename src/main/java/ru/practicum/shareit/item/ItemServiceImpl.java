package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.BookingStatus;
import ru.practicum.shareit.booking.dao.BookingRepository;
import ru.practicum.shareit.booking.dto.ShortBookingView;
import ru.practicum.shareit.comment.Comment;
import ru.practicum.shareit.comment.CommentMapper;
import ru.practicum.shareit.comment.CommentRepository;
import ru.practicum.shareit.comment.Dto.CommentCreationDto;
import ru.practicum.shareit.comment.Dto.CommentOutputDto;
import ru.practicum.shareit.exception.CommentForbidden;
import ru.practicum.shareit.exception.ItemCouldntBeModified;
import ru.practicum.shareit.exception.ItemNotFoundException;
import ru.practicum.shareit.exception.UserNotFoundException;
import ru.practicum.shareit.item.dao.ItemRepository;
import ru.practicum.shareit.item.dto.ItemCreationDto;
import ru.practicum.shareit.item.dto.ItemOutputDto;
import ru.practicum.shareit.item.dto.ItemUpdateDto;
import ru.practicum.shareit.item.dto.ItemUserOutputDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.dao.UserRepository;

import javax.validation.Valid;
import java.text.MessageFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {

    //    private final ItemStorage itemStorage;
    private final ItemRepository itemStorage;
    //    private final UserStorage userStorage;
    private final UserRepository userStorage;

    private final BookingRepository bookingStorage;

    private final CommentRepository commentRepository;

    public ItemOutputDto createItem(Integer userId, ItemCreationDto itemCreationDto) {
        User user = userStorage.findById(userId).orElseThrow(() ->
                new UserNotFoundException(MessageFormat.format("User with id {0} not found", userId)));
        Item item = ItemMapper.toItem(itemCreationDto, user);
        Item savedItem = itemStorage.save(item);
        return ItemMapper.toItemOutDto(savedItem);
    }

    public ItemOutputDto updateItem(Integer itemId, Integer userId, ItemUpdateDto itemUpdateDto) {
        Item itemForUpdate = itemStorage.findById(itemId).orElseThrow(
                () -> new ItemNotFoundException(MessageFormat.format("Item with id {0} not found", itemId)));
        User user = userStorage.findById(userId).orElseThrow(() ->
                new UserNotFoundException(MessageFormat.format("User with id {0} not found", userId)));
        if (!Objects.equals(itemForUpdate.getOwner(), user)) {
            throw new ItemCouldntBeModified(MessageFormat.format("User with id {0} can't modify foreign item", userId));
        }

        Item editedItem = ItemMapper.toItem(itemForUpdate, itemUpdateDto, user);

        Item updatedItem = itemStorage.save(editedItem);
        return ItemMapper.toItemOutDto(updatedItem);
    }

    public ItemUserOutputDto getItemById(Integer itemId, Integer userId) {

        Item item = itemStorage.findById(itemId).orElseThrow(() ->
                        new ItemNotFoundException(MessageFormat.format("Item with id={0} not found", itemId))
        );
        List<CommentOutputDto> commentsByItemId = commentRepository.getCommentsByItemId(itemId);
        ItemUserOutputDto userItemOutDto = ItemMapper.toUserItemOutDto(item, commentsByItemId);

        if (userId.equals(item.getOwner().getId())) {
            ShortBookingView lastBooking = bookingStorage.findLastItemBooking(itemId);
            userItemOutDto.setLastBooking(lastBooking);
            ShortBookingView nextBooking = bookingStorage.findNextItemBooking(itemId);
            userItemOutDto.setNextBooking(nextBooking);
        }
        return userItemOutDto;
    }

    public Collection<ItemUserOutputDto> getAllUserItems(Integer userId) {
        Collection<ItemUserOutputDto> result = new ArrayList<>();
        itemStorage.findByOwner_Id(userId).forEach(
                item -> {
                    Integer itemId = item.getId();
                    List<CommentOutputDto> commentsByItemId = commentRepository.getCommentsByItemId(itemId);
                    ItemUserOutputDto itemUserOutputDto = ItemMapper.toUserItemOutDto(item, commentsByItemId);
                    ShortBookingView lastBooking = bookingStorage.findLastItemBooking(itemId);
                    itemUserOutputDto.setLastBooking(lastBooking);
                    ShortBookingView nextBooking = bookingStorage.findNextItemBooking(itemId);
                    itemUserOutputDto.setNextBooking(nextBooking);
                    result.add(itemUserOutputDto);
                }
        );
        return result;
    }

    public Collection<ItemOutputDto> getItemByNameOrDescription(String text) {
        return itemStorage.findByNameOrDescription(text).stream()
                .map(ItemMapper::toItemOutDto)
                .collect(Collectors.toList());
    }

    @Override
    public CommentOutputDto saveComment(Integer commentatorId, Integer itemId, @Valid CommentCreationDto commentCreationDto) {
        Item item = itemStorage.findById(itemId).orElseThrow(() ->
                new ItemNotFoundException(MessageFormat.format("Item with id={0} not found", itemId))
        );

        User commentator = userStorage.findById(commentatorId).orElseThrow(() ->
                new UserNotFoundException(MessageFormat.format("User with id {0} not found", commentatorId)));
        Integer cntOfFinishedItemBookings = bookingStorage.countFinishedItemBookingsByBooker(
                commentatorId,
                itemId,
                BookingStatus.APPROVED,
                LocalDateTime.now()
        );
        if (cntOfFinishedItemBookings < 1) {
            throw new CommentForbidden("You cant comment this item!");
        }

        Comment commentForSave = CommentMapper.toComment(commentCreationDto, item, commentator);
        Comment savedComment = commentRepository.save(commentForSave);
        return CommentMapper.toCommentOutputDto(savedComment);
    }
}
