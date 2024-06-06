package ru.practicum.shareit.item.dao;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.jdbc.Sql;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.ItemRequest;
import ru.practicum.shareit.request.dao.ItemRequestRepository;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.dao.UserRepository;

import java.time.LocalDateTime;
import java.util.List;

@DataJpaTest
@Sql(scripts = "/schema-test.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
class ItemRepositoryTest {
    @Autowired
    ItemRepository itemRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    ItemRequestRepository itemRequestRepository;

    private User itemOwner;

    @BeforeEach
    void setUp() {
        User itemOwnerForSave = User.builder()
                .name("Test itemOwner user")
                .email("itemOwner@test.com")
                .build();
        itemOwner = userRepository.save(itemOwnerForSave);
        Item testItem = Item.builder()
                .name("Test item")
                .description("Test item description")
                .owner(itemOwner)
                .available(true)
                .build();
        itemRepository.save(testItem);

        Item anotherTestItem = Item.builder()
                .name("another item")
                .description("another item description")
                .owner(itemOwner)
                .available(true)
                .build();
        itemRepository.save(anotherTestItem);
    }

    @Test
    void findByNameOrDescription() {
        List<Item> foundItems = itemRepository.findByNameOrDescription("test", PageRequest.of(0, 10));
        Assertions.assertEquals(1, foundItems.size());
        Assertions.assertEquals("Test item", foundItems.get(0).getName());

    }

    @Test
    void findByOwner_Id() {
        List<Item> byOwnerId = itemRepository.findByOwner_Id(itemOwner.getId(), PageRequest.of(0, 11));
        Assertions.assertEquals(2, byOwnerId.size());
    }

    @Test
    void findByRequestId() {
        User reqestorUserForSave = User.builder()
                .name("Test reqestor User user")
                .email("reqestorUser@test.com")
                .build();
        User reqestorUser = userRepository.save(reqestorUserForSave);
        ItemRequest testItemRequest = ItemRequest.builder()
                .created(LocalDateTime.now())
                .requestor(reqestorUser)
                .build();
        itemRequestRepository.save(testItemRequest);
        Item testItemWithRequest = Item.builder()
                .name("Test item for request")
                .description("Test item for request description")
                .owner(itemOwner)
                .available(true)
                .request(testItemRequest)
                .build();
        itemRepository.save(testItemWithRequest);

        List<Item> byRequestId = itemRepository.findByRequestId(testItemRequest.getId());
        Assertions.assertEquals(1, byRequestId.size());
        Assertions.assertEquals(testItemWithRequest, byRequestId.get(0));
    }
}