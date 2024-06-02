package ru.practicum.shareit.request.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.request.ItemRequest;

import java.util.List;

@Repository
public interface ItemRequestRepository extends JpaRepository<ItemRequest, Integer> {

   List<ItemRequest> findAllByRequestorIdOrderByCreatedDesc(Integer requestorId);

   Page<ItemRequest> findAll(Pageable pageable);

   Page<ItemRequest> findAllByRequestor_IdNot(Integer requestorId, Pageable pageable);

}
