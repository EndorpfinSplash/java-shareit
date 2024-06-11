package ru.practicum.shareit.request.dao;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.jdbc.Sql;
import ru.practicum.shareit.request.ItemRequest;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.dao.UserRepository;

import java.time.LocalDateTime;
import java.util.List;

@DataJpaTest
@Sql(scripts = "/schema-test.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
class ItemRequestRepositoryTest {
    @Autowired
    UserRepository userRepository;

    @Autowired
    ItemRequestRepository itemRequestRepository;

    private User requestorUser;
    private ItemRequest testItemRequest1;
    private ItemRequest testItemRequest2;

    @BeforeEach
    void setUp() {
        User requestorUserForSave = User.builder()
                .name("Test reqestor User user")
                .email("reqestorUser@test.com")
                .build();
        requestorUser = userRepository.save(requestorUserForSave);

        testItemRequest1 = ItemRequest.builder()
                .created(LocalDateTime.now())
                .requestor(requestorUser)
                .build();
        itemRequestRepository.save(testItemRequest1);

        testItemRequest2 = ItemRequest.builder()
                .created(LocalDateTime.now().plusDays(1))
                .requestor(requestorUser)
                .build();
        itemRequestRepository.save(testItemRequest2);
    }

    @Test
    void findAllByRequestorIdOrderByCreatedDesc() {
        List<ItemRequest> allByRequestorIdOrderByCreatedDesc = itemRequestRepository.findAllByRequestorIdOrderByCreatedDesc(
                requestorUser.getId()
        );
        Assertions.assertEquals(2, allByRequestorIdOrderByCreatedDesc.size());
        Assertions.assertEquals(testItemRequest2, allByRequestorIdOrderByCreatedDesc.get(0));
        Assertions.assertEquals(testItemRequest1, allByRequestorIdOrderByCreatedDesc.get(1));
    }

    @Test
    void findAllByRequestor_IdNot() {
        List<ItemRequest> allByRequestorIdNot = itemRequestRepository.findAllByRequestor_IdNot(requestorUser.getId(), PageRequest.of(0, 10));
        Assertions.assertEquals(0, allByRequestorIdNot.size());

        User anotherRequestorUserForSave = User.builder()
                .name("Test another reqestor User user")
                .email("anotherreqestorUser@test.com")
                .build();
        User anotherUser = userRepository.save(anotherRequestorUserForSave);

        ItemRequest anotherUserItemRequest = ItemRequest.builder()
                .created(LocalDateTime.now())
                .requestor(anotherUser)
                .build();
        itemRequestRepository.save(anotherUserItemRequest);

        allByRequestorIdNot = itemRequestRepository.findAllByRequestor_IdNot(requestorUser.getId(), PageRequest.of(0, 10));
        Assertions.assertEquals(1, allByRequestorIdNot.size());

    }
}