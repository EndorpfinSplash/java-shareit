package ru.practicum.shareit.item.dto;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareit.comment.dto.CommentOutputDto;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
class ItemUserOutputDtoTest {

    @Autowired
    JacksonTester<ItemUserOutputDto> jacksonTester;

    @Test
    void testSerialization() throws IOException {
        CommentOutputDto commentOutputDto = new CommentOutputDto(2, "Test comment", "Test Author", LocalDateTime.now());
        ItemUserOutputDto itemUserOutputDto = ItemUserOutputDto.builder()
                .id(1)
                .name("Test Item user output")
                .description("Test Item output description")
                .available(true)
                .comments(List.of(commentOutputDto))
                .build();

        JsonContent<ItemUserOutputDto> itemUserOutputDtoJsonContent = jacksonTester.write(itemUserOutputDto);

        assertThat(itemUserOutputDtoJsonContent).isNotNull();

        assertThat(itemUserOutputDtoJsonContent).hasJsonPath("$.id");
        assertThat(itemUserOutputDtoJsonContent).extractingJsonPathNumberValue("$.id").isEqualTo(itemUserOutputDto.getId());

        assertThat(itemUserOutputDtoJsonContent).hasJsonPath("$.name");
        assertThat(itemUserOutputDtoJsonContent).extractingJsonPathStringValue("$.name").isEqualTo(itemUserOutputDto.getName());

        assertThat(itemUserOutputDtoJsonContent).hasJsonPath("$.description");
        assertThat(itemUserOutputDtoJsonContent).extractingJsonPathStringValue("$.description").isEqualTo(itemUserOutputDto.getDescription());

        assertThat(itemUserOutputDtoJsonContent).hasJsonPath("$.available");
        assertThat(itemUserOutputDtoJsonContent).extractingJsonPathBooleanValue("$.available").isEqualTo(itemUserOutputDto.getAvailable());

        assertThat(itemUserOutputDtoJsonContent).hasJsonPath("$.lastBooking");
        assertThat(itemUserOutputDtoJsonContent).hasJsonPath("$.nextBooking");

        assertThat(itemUserOutputDtoJsonContent).hasJsonPath("$.comments");
        assertThat(itemUserOutputDtoJsonContent).hasJsonPath("$.comments.[0].id");
        assertThat(itemUserOutputDtoJsonContent).hasJsonPath("$.comments.[0].text");
        assertThat(itemUserOutputDtoJsonContent).hasJsonPath("$.comments.[0].authorName");
    }

}