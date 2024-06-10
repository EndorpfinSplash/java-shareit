package ru.practicum.shareit.comment;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;
import ru.practicum.shareit.comment.dto.CommentOutputDto;
import ru.practicum.shareit.item.dao.ItemRepository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.dao.UserRepository;

import java.time.LocalDateTime;
import java.util.List;

@DataJpaTest
@Sql(scripts = "/schema-test.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
class CommentRepositoryTest {

    @Autowired
    CommentRepository commentRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ItemRepository itemRepository;


    @Test
    void getCommentsByItemId() {
        User commentatorForSave = User.builder()
                .name("Test commentator user")
                .email("commentator@test.com")
                .build();
        User testCommentator = userRepository.save(commentatorForSave);
        User itemOwnerForSave = User.builder()
                .name("Test itemOwner user")
                .email("itemOwner@test.com")
                .build();
        User itemOwner = userRepository.save(itemOwnerForSave);
        Item commentedItemForSave = Item.builder()
                .name("Test commentated item")
                .description("Test commentated item description")
                .owner(itemOwner)
                .available(true)
                .build();
        Item commentedItem = itemRepository.save(commentedItemForSave);

        Comment comment = Comment.builder()
                .text("Test comment")
                .author(testCommentator)
                .item(commentedItem)
                .created(LocalDateTime.now())
                .build();
        commentRepository.save(comment);

        List<CommentOutputDto> commentsByItemId = commentRepository.getCommentsByItemId(commentedItem.getId());

        Assertions.assertEquals(1, commentsByItemId.size());
        Assertions.assertEquals("Test comment", commentsByItemId.get(0).getText());
    }
}