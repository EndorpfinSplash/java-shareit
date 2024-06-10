package ru.practicum.shareit.item.dto;

import lombok.Builder;
import lombok.Data;
import ru.practicum.shareit.booking.dto.ShortBookingView;
import ru.practicum.shareit.comment.dto.CommentOutputDto;

import java.util.List;

@Data
@Builder
public class ItemUserOutputDto {
    private Integer id;
    private String name;
    private String description;
    private Boolean available;
    private ShortBookingView lastBooking;
    private ShortBookingView nextBooking;
    private List<CommentOutputDto> comments;

}
