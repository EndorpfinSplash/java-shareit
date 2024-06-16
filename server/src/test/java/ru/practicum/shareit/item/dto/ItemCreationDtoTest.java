package ru.practicum.shareit.item.dto;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
class ItemCreationDtoTest {

    @Autowired
    JacksonTester<ItemCreationDto> jacksonTester;


    @Test
    void serialize() throws IOException {
        ItemCreationDto itemCreationDto = new ItemCreationDto("Test Item name", "Test Item description", true, 1);
        JsonContent<ItemCreationDto> creationDtoJsonContent = jacksonTester.write(itemCreationDto);

        assertThat(creationDtoJsonContent).isNotNull();
        assertThat(creationDtoJsonContent).hasJsonPath("$.name");
        assertThat(creationDtoJsonContent).extractingJsonPathStringValue("$.name").isEqualTo(itemCreationDto.getName());

        assertThat(creationDtoJsonContent).hasJsonPath("$.description");
        assertThat(creationDtoJsonContent).extractingJsonPathStringValue("$.description").isEqualTo(itemCreationDto.getDescription());

        assertThat(creationDtoJsonContent).hasJsonPath("$.available");
        assertThat(creationDtoJsonContent).extractingJsonPathBooleanValue("$.available").isEqualTo(itemCreationDto.getAvailable());

        assertThat(creationDtoJsonContent).hasJsonPath("$.requestId");
        assertThat(creationDtoJsonContent).extractingJsonPathNumberValue("$.requestId").isEqualTo(itemCreationDto.getRequestId());

    }
}