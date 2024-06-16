package ru.practicum.shareit.request.dto;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareit.item.dto.ItemForRequestorOutputDto;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
class RequestWithItemsOutputDtoTest {
    @Autowired
    JacksonTester<RequestWithItemsOutputDto> jacksonTester;

    @Test
    void serializesToJson() throws Exception {
        ItemForRequestorOutputDto itemForRequestorOutputDto = ItemForRequestorOutputDto.builder()
                .id(2)
                .name("Test Item")
                .description("Test Item description")
                .available(true)
                .requestId(10)
                .build();
        RequestWithItemsOutputDto requestWithItemsOutputDto = RequestWithItemsOutputDto.builder()
                .id(10)
                .description("Test Item Request description")
                .created(LocalDateTime.now())
                .items(List.of(itemForRequestorOutputDto))
                .build();

        JsonContent<RequestWithItemsOutputDto> requestWithItemsOutputDtoJsonContent = jacksonTester.write(requestWithItemsOutputDto);

        assertThat(requestWithItemsOutputDtoJsonContent).isNotNull();

        assertThat(requestWithItemsOutputDtoJsonContent).hasJsonPath("$.id");
        assertThat(requestWithItemsOutputDtoJsonContent).extractingJsonPathNumberValue("$.id").isEqualTo(requestWithItemsOutputDto.getId());

        assertThat(requestWithItemsOutputDtoJsonContent).hasJsonPath("$.description");
        assertThat(requestWithItemsOutputDtoJsonContent).extractingJsonPathStringValue("$.description").isEqualTo(requestWithItemsOutputDto.getDescription());

        assertThat(requestWithItemsOutputDtoJsonContent).hasJsonPath("$.created");

        assertThat(requestWithItemsOutputDtoJsonContent).hasJsonPath("$.items");
        assertThat(requestWithItemsOutputDtoJsonContent).hasJsonPath("$.items.[0].id");
        assertThat(requestWithItemsOutputDtoJsonContent).hasJsonPath("$.items.[0].name");
        assertThat(requestWithItemsOutputDtoJsonContent).hasJsonPath("$.items.[0].description");
        assertThat(requestWithItemsOutputDtoJsonContent).hasJsonPath("$.items.[0].available");
        assertThat(requestWithItemsOutputDtoJsonContent).hasJsonPath("$.items.[0].requestId");

        assertThat(requestWithItemsOutputDtoJsonContent).extractingJsonPathNumberValue("$.items.[0].id");
        assertThat(requestWithItemsOutputDtoJsonContent).extractingJsonPathStringValue("$.items.[0].name").isEqualTo("Test Item");
        assertThat(requestWithItemsOutputDtoJsonContent).extractingJsonPathStringValue("$.items.[0].description").isEqualTo("Test Item description");
        assertThat(requestWithItemsOutputDtoJsonContent).extractingJsonPathBooleanValue("$.items.[0].available").isEqualTo(true);
        assertThat(requestWithItemsOutputDtoJsonContent).extractingJsonPathNumberValue("$.items.[0].requestId").isEqualTo(10);
    }

}