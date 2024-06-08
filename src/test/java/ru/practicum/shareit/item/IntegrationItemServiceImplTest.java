package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.item.dto.ItemCreationDto;
import ru.practicum.shareit.item.dto.ItemUserOutputDto;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.dao.UserRepository;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

@Transactional
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@SpringBootTest(
        properties = "spring.datasource.username=test",
        webEnvironment = SpringBootTest.WebEnvironment.NONE)
class IntegrationItemServiceImplTest {


    private final UserRepository userRepository;
    private final ItemService itemService;


    @Test
    void getUserItems() {
        User ownerItem = new User(1, "Test Item Owner ", "Tester@mail.com");
        userRepository.save(ownerItem);
        ItemCreationDto itemCreationDto = new ItemCreationDto("Test item", "Item description", true, null);
        itemService.createItem(ownerItem.getId(), itemCreationDto);
        List<ItemUserOutputDto> userItems = new ArrayList<>(itemService.getUserItems(ownerItem.getId(), 0, 10));
        assertThat(userItems, notNullValue());
        assertThat(userItems.get(0).getName(), equalTo(itemCreationDto.getName()));
        assertThat(userItems.get(0).getDescription(), equalTo(itemCreationDto.getDescription()));
        assertThat(userItems.get(0).getAvailable(), equalTo(itemCreationDto.getAvailable()));

    }
}