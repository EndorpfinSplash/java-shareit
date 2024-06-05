package ru.practicum.shareit.comment;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.practicum.shareit.comment.Dto.CommentCreationDto;
import ru.practicum.shareit.comment.Dto.CommentOutputDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;

import java.time.LocalDateTime;

class CommentMapperTest {
    final User originTestUser = User.builder()
            .id(2)
            .name("Test User")
            .build();
    final Item originTestItem = Item.builder()
            .id(3)
            .name("Test Item")
            .build();

    @Test
    void toComment() {
        CommentCreationDto originCommentCreationDto = CommentCreationDto.builder()
                .text("test text")
                .build();

        Comment resultComment = CommentMapper.toComment(originCommentCreationDto, originTestItem, originTestUser);

        Assertions.assertNotNull(resultComment);
        Assertions.assertEquals(originCommentCreationDto.getText(), resultComment.getText());
        Assertions.assertEquals(originTestUser, resultComment.getAuthor());
        Assertions.assertEquals(originTestItem, resultComment.getItem());

    }

    @Test
    void toCommentOutputDto() {
        Comment originComment = Comment.builder()
                .id(1)
                .author(originTestUser)
                .text("test text")
                .item(originTestItem)
                .created(LocalDateTime.now())
                .build();

        CommentOutputDto reusltCommentOutputDto = CommentMapper.toCommentOutputDto(originComment);
        Assertions.assertNotNull(reusltCommentOutputDto);
        Assertions.assertEquals(originComment.getId(), reusltCommentOutputDto.getId());
        Assertions.assertEquals(originComment.getAuthor().getName(), reusltCommentOutputDto.getAuthorName());
        Assertions.assertEquals(originComment.getText(), reusltCommentOutputDto.getText());
        Assertions.assertEquals(originComment.getCreated(), reusltCommentOutputDto.getCreated());
    }
}