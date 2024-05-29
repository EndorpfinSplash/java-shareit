package ru.practicum.shareit.comment.Dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class CommentCreationDto {
    @NotBlank
    private String text;
}
