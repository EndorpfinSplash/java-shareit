package ru.practicum.shareit.booking.dto;


import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareit.booking.BookingStatus;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;

import java.io.IOException;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
class BookingOutputDtoTest {

    @Autowired
    private JacksonTester<BookingOutputDto> jsonbTester;

    @Test
    void testSerialization() throws IOException {
        User booker = User.builder()
                .id(2)
                .name("John Doe")
                .email("john.doe@example.com")
                .build();
        Item item = Item.builder()
                .id(3)
                .name("test item")
                .build();
        BookingOutputDto bookingOutputDto = BookingOutputDto.builder()
                .id(1)
                .booker(booker)
                .item(item)
                .status(BookingStatus.WAITING)
                .start(LocalDateTime.now())
                .end(LocalDateTime.now().plusDays(1))
                .build();
        JsonContent<BookingOutputDto> bookingOutputDtoJsonContent = jsonbTester.write(bookingOutputDto);
        assertThat(bookingOutputDtoJsonContent).isNotNull();
        assertThat(bookingOutputDtoJsonContent).hasJsonPath("$.id");
        assertThat(bookingOutputDtoJsonContent).hasJsonPath("$.start");
        assertThat(bookingOutputDtoJsonContent).hasJsonPath("$.end");
        assertThat(bookingOutputDtoJsonContent).hasJsonPath("$.booker");
        assertThat(bookingOutputDtoJsonContent).hasJsonPath("$.item");
        assertThat(bookingOutputDtoJsonContent).hasJsonPath("$.status");
        assertThat(bookingOutputDtoJsonContent).extractingJsonPathNumberValue("$.id").isEqualTo(bookingOutputDto.getId());
        assertThat(bookingOutputDtoJsonContent).hasJsonPathValue("$.start");
        assertThat(bookingOutputDtoJsonContent).hasJsonPathValue("$.end");
        assertThat(bookingOutputDtoJsonContent).extractingJsonPathNumberValue("$.booker.id").isEqualTo(bookingOutputDto.getBooker().getId());
        assertThat(bookingOutputDtoJsonContent).extractingJsonPathStringValue("$.booker.name").isEqualTo(bookingOutputDto.getBooker().getName());
        assertThat(bookingOutputDtoJsonContent).extractingJsonPathNumberValue("$.item.id").isEqualTo(bookingOutputDto.getItem().getId());
        assertThat(bookingOutputDtoJsonContent).extractingJsonPathStringValue("$.item.name").isEqualTo(bookingOutputDto.getItem().getName());
        assertThat(bookingOutputDtoJsonContent).extractingJsonPathStringValue("$.status").isEqualTo(bookingOutputDto.getStatus().name());
    }
}
