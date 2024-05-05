package ru.practicum.shareit.item.model;

import lombok.*;
import ru.practicum.shareit.request.ItemRequest;
import ru.practicum.shareit.user.User;


@Data
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Item {
    private Integer id;
    private String name;
    private String description;
    private Boolean available;
    private User owner;
    private ItemRequest request;

    public Boolean isAvailable() {
        return available;
    }
}
