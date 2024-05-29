package ru.practicum.shareit.booking.dao;


import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.booking.Booking;

import java.time.LocalDateTime;
import java.util.List;


@Repository
public interface BookingRepository extends JpaRepository<Booking, Integer> {
    List<Booking> findByBooker_IdAndEndIsBefore(Integer bookerId, LocalDateTime end, Sort sort);

    List<Booking> findBookingsByItem_Id(Integer itemId);

    @Query(" select b from Booking b join b.booker as u " +
            " where u.id = ?1 " +
            " order by b.start desc ")
    List<Booking> findByBooker_IdOrderByStartDesc(Integer booker_Id);

    @Query(" select b from Booking b join b.booker as u " +
            " where u.id = ?1 and b.bookingStatus = ?2 " +
            " order by b.start desc ")
    List<Booking> findByBooker_IdAndBookingStatusOrderByStartDesc(Integer bookerId, String bookingStatus);

    @Query(" select b from Booking b join b.booker as u " +
            " where u.id = ?1 " +
            " and current_timestamp between b.start and b.end " +
            " order by b.start desc ")
    List<Booking> findByBooker_CurrentBookingsOrderByStartDesc(Integer bookerId);


    @Query(" select b from Booking b join b.booker as u " +
            " where u.id = ?1 " +
            " and b.end <current_timestamp" +
            " order by b.start desc ")
    List<Booking> findByBooker_PastBookingsOrderByStartDesc(Integer bookerId);


    @Query(" select b from Booking b join b.booker as u " +
            " where u.id = ?1 " +
            " and b.start > current_timestamp" +
            " order by b.start desc ")
    List<Booking> findByBooker_FutureBookingsOrderByStartDesc(Integer bookerId);


    @Query(" select b from Booking b join b.item as i join i.owner o " +
            " where o.id = ?1 " +
            "  order by b.start desc "
    )
    List<Booking> findAllByOwner(Integer ownerId);

    @Query(" select b from Booking b join b.item as i join i.owner as o " +
            " where o.id = ?1 " +
            "   and b.bookingStatus = ?2 " +
            " order by b.start desc "
    )
    List<Booking> findByOwnerAndStatus(Integer ownerId, String bookingStatus);

    @Query(" select b from Booking b join b.item as i join i.owner o" +
            " where o.id = ?1 " +
            " and current_timestamp between b.start and b.end " +
            " order by b.start desc "
    )
    List<Booking> findByOwnerCurrentBookingsOrderByStartDesc(Integer ownerId);

    @Query(" select b from Booking b join b.item as i join i.owner o " +
            " where o.id = ?1 " +
            " and b.end < current_timestamp " +
            " order by b.start desc "
    )
    List<Booking> findByOwnerPast(Integer ownerId);

    @Query(" select b from Booking b join b.item as i join i.owner o " +
            " where o.id = ?1 " +
            " and b.start > current_timestamp " +
            " order by b.start desc "
    )
    List<Booking> findByOwnerFuture(Integer ownerId);

}
