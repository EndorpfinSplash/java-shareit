package ru.practicum.shareit.comment;

import ru.practicum.shareit.comment.dto.CommentCreationDto;
import ru.practicum.shareit.comment.dto.CommentOutputDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;

import java.time.LocalDateTime;

public class CommentMapper {

    public static Comment toComment(CommentCreationDto commentCreationDto, Item item, User author) {
        return Comment.builder()
                .text(commentCreationDto.getText())
                .item(item)
                .author(author)
                .created(LocalDateTime.now())
                .build();
    }


    public static CommentOutputDto toCommentOutputDto(Comment savedComment) {
        return CommentOutputDto.builder()
                .id(savedComment.getId())
                .text(savedComment.getText())
                .authorName(savedComment.getAuthor().getName())
                .created(savedComment.getCreated())
                .build();
    }
}
