package ru.practicum.shareit.booking.dao;


import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.booking.Booking;

import java.time.LocalDateTime;
import java.util.List;


@Repository
public interface BookingRepository extends JpaRepository<Booking, Integer> {
    List<Booking> findByBooker_IdAndEndIsBefore(Integer bookerId, LocalDateTime end, Sort sort);

    List<Booking> findBookingsByItem_Id(Integer itemId);
}
