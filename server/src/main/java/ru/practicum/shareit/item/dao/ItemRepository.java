package ru.practicum.shareit.item.dao;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

@Repository
public interface ItemRepository extends JpaRepository<Item, Integer> {
    @Query(" select i from Item i " +
            "where upper(i.name) like upper(concat('%', ?1, '%')) " +
            " or upper(i.description) like upper(concat('%', ?1, '%')) " +
            " and i.available is true "
    )
    List<Item> findByNameOrDescription(String text, Pageable pageable);

    List<Item> findByOwner_IdOrderById(Integer ownerId, Pageable pageable);

    List<Item> findByRequestId(Integer requestId);

}
