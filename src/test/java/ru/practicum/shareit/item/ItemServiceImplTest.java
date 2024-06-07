package ru.practicum.shareit.item;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import ru.practicum.shareit.booking.BookingStatus;
import ru.practicum.shareit.booking.dao.BookingRepository;
import ru.practicum.shareit.booking.dto.ShortBookingView;
import ru.practicum.shareit.comment.Comment;
import ru.practicum.shareit.comment.CommentRepository;
import ru.practicum.shareit.comment.Dto.CommentCreationDto;
import ru.practicum.shareit.comment.Dto.CommentOutputDto;
import ru.practicum.shareit.exception.ItemCouldntBeModified;
import ru.practicum.shareit.exception.ItemRequestNotFoundException;
import ru.practicum.shareit.item.dao.ItemRepository;
import ru.practicum.shareit.item.dto.ItemCreationDto;
import ru.practicum.shareit.item.dto.ItemOutputDto;
import ru.practicum.shareit.item.dto.ItemUpdateDto;
import ru.practicum.shareit.item.dto.ItemUserOutputDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.dao.ItemRequestRepository;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.dao.UserRepository;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ItemServiceImplTest {

    @Mock
    private ItemRepository itemRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private BookingRepository bookingStorage;
    @Mock
    private CommentRepository commentRepository;
    @Mock
    private ItemRequestRepository itemRequestRepository;

    @InjectMocks
    private ItemServiceImpl itemService;

    private User ownerItem;
    private Item testItem;

    @BeforeEach
    void setUp() {
        ownerItem = new User(2, "Test Item Owner ", "Tester@mail.com");
        testItem = new Item(3, "Test item", "Item description", true, ownerItem, null);
    }

    @Test
    void createItem() {
        when(userRepository.findById(ownerItem.getId())).thenReturn(Optional.of(ownerItem));
        when(itemRepository.save(any(Item.class))).thenAnswer(invocationOnMock -> invocationOnMock.getArgument(0));
        ItemCreationDto itemCreationDto = ItemCreationDto.builder()
                .name("Test item")
                .description("Item description")
                .available(true)
                .build();
        ItemOutputDto savedItem = itemService.createItem(ownerItem.getId(), itemCreationDto);

        verify(itemRepository, times(1)).save(any(Item.class));
        Assertions.assertEquals(itemCreationDto.getName(), savedItem.getName());
        Assertions.assertEquals(itemCreationDto.getDescription(), savedItem.getDescription());
        Assertions.assertEquals(itemCreationDto.getAvailable(), savedItem.getAvailable());

        itemCreationDto.setRequestId(100);
        Assertions.assertThrows(ItemRequestNotFoundException.class,
                () -> itemService.createItem(ownerItem.getId(), itemCreationDto));
    }

    @Test
    void updateItem() {
        when(userRepository.findById(ownerItem.getId())).thenReturn(Optional.of(ownerItem));
        when(itemRepository.findById(testItem.getId())).thenReturn(Optional.of(testItem));
        when(itemRepository.save(any(Item.class))).thenAnswer(invocationOnMock -> invocationOnMock.getArgument(0));
        ItemUpdateDto itemUpdateDto = ItemUpdateDto.builder()
                .available(false)
                .name("New name")
                .description("New description")
                .build();
        ItemOutputDto updateItem = itemService.updateItem(testItem.getId(), ownerItem.getId(), itemUpdateDto);
        verify(itemRepository, times(1)).save(any(Item.class));
        Assertions.assertEquals(itemUpdateDto.getName(), updateItem.getName());
        Assertions.assertEquals(itemUpdateDto.getDescription(), updateItem.getDescription());
        Assertions.assertEquals(itemUpdateDto.getAvailable(), updateItem.getAvailable());

        User anotherUser = new User(200, "anotherUser ", "anotherUser@mail.com");
        when(userRepository.findById(anotherUser.getId())).thenReturn(Optional.of(anotherUser));
        Assertions.assertThrows(ItemCouldntBeModified.class,
                () -> itemService.updateItem(testItem.getId(), anotherUser.getId(), itemUpdateDto));
    }

    @Test
    void getItemById() {
        when(itemRepository.findById(testItem.getId())).thenReturn(Optional.of(testItem));
        when(bookingStorage.findLastItemBooking(testItem.getId())).thenReturn(new ShortBookingView() {
            @Override
            public Integer getId() {
                return 0;
            }

            @Override
            public Integer getBookerId() {
                return 0;
            }
        });
        ItemUserOutputDto itemById = itemService.getItemById(testItem.getId(), ownerItem.getId());
        Assertions.assertEquals(testItem.getId(), itemById.getId());
        Assertions.assertEquals(testItem.getName(), itemById.getName());
        Assertions.assertEquals(testItem.getDescription(), itemById.getDescription());
        verify(itemRepository, times(1)).findById(testItem.getId());
        verify(commentRepository, times(1)).getCommentsByItemId(testItem.getId());
        verify(bookingStorage, times(1)).findLastItemBooking(testItem.getId());
        verify(bookingStorage, times(1)).findNextItemBooking(testItem.getId());
    }

    @Test
    void getAllUserItems() {
        itemService.getAllUserItems(ownerItem.getId(), 0, 10);
        verify(itemRepository, times(1)).findByOwner_Id(ownerItem.getId(), PageRequest.of(0, 10));
    }

    @Test
    void getItemByNameOrDescription() {
        itemService.getItemByNameOrDescription("test text", 0, 10);
        verify(itemRepository, times(1)).findByNameOrDescription("test text", PageRequest.of(0, 10));
    }

    @Test
    void saveComment() {
        when(commentRepository.save(any(Comment.class))).thenAnswer(invocationOnMock -> invocationOnMock.getArgument(0));
        when(itemRepository.findById(testItem.getId())).thenReturn(Optional.of(testItem));
        User commentator = new User(4, "commentator", "commentator@mail.com");
        when(userRepository.findById(commentator.getId())).thenReturn(Optional.of(commentator));
        when(bookingStorage.countFinishedItemBookingsByBooker(
                anyInt(), anyInt(), any(BookingStatus.class), any(LocalDateTime.class))
        ).thenReturn(1);
        CommentCreationDto commentCreationDto = new CommentCreationDto("test comment");
        CommentOutputDto commentOutputDto = itemService.saveComment(commentator.getId(), testItem.getId(), commentCreationDto);
        verify(commentRepository, times(1)).save(any(Comment.class));
        Assertions.assertEquals(commentCreationDto.getText(), commentOutputDto.getText());
        Assertions.assertEquals(commentator.getName(), commentOutputDto.getAuthorName());
    }
}