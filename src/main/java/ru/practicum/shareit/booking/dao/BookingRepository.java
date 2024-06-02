package ru.practicum.shareit.booking.dao;


import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.BookingStatus;
import ru.practicum.shareit.booking.dto.ShortBookingView;

import java.time.LocalDateTime;
import java.util.List;


@Repository
public interface BookingRepository extends JpaRepository<Booking, Integer> {

    @Query(" select count(b) from Booking b join b.booker as u join b.item i " +
            " where u.id = ?1 and i.id = ?2 " +
            " and b.bookingStatus = ?3 and b.end <= ?4 ")
    Integer countFinishedItemBookingsByBooker(Integer booker,
                                              Integer item,
                                              BookingStatus bookingStatus,
                                              LocalDateTime currentDateTime);

    @Query(" select b from Booking b join b.booker as u " +
            " where u.id = ?1 " +
            " order by b.start desc ")
    List<Booking> findByBooker_IdOrderByStartDesc(Integer bookerId, Pageable pageable);

    @Query(" select b from Booking b join b.booker as u " +
            " where u.id = ?1 and b.bookingStatus = ?2 " +
            " order by b.start desc ")
    List<Booking> findByBooker_IdAndBookingStatusOrderByStartDesc(Integer bookerId, BookingStatus bookingStatus, Pageable pageable);

    @Query(" select b from Booking b join b.booker as u " +
            " where u.id = ?1 " +
            " and current_timestamp between b.start and b.end " +
            " order by b.start desc ")
    List<Booking> findByBooker_CurrentBookingsOrderByStartDesc(Integer bookerId, Pageable pageable);


    @Query(" select b from Booking b join b.booker as u " +
            " where u.id = ?1 " +
            " and b.end <current_timestamp" +
            " order by b.start desc ")
    List<Booking> findByBooker_PastBookingsOrderByStartDesc(Integer bookerId, Pageable pageable);


    @Query(" select b from Booking b join b.booker as u " +
            " where u.id = ?1 " +
            " and b.start > current_timestamp" +
            " order by b.start desc ")
    List<Booking> findByBooker_FutureBookingsOrderByStartDesc(Integer bookerId, Pageable pageable);


    @Query(" select b from Booking b join b.item as i join i.owner o " +
            " where o.id = ?1 " +
            "  order by b.start desc "
    )
    List<Booking> findAllByOwner(Integer ownerId, Pageable pageable);

    @Query(" select b from Booking b join b.item as i join i.owner as o " +
            " where o.id = ?1 " +
            "   and b.bookingStatus = ?2 " +
            " order by b.start desc "
    )
    List<Booking> findByOwnerAndStatus(Integer ownerId, BookingStatus bookingStatus, Pageable pageable);

    @Query(" select b from Booking b join b.item as i join i.owner o" +
            " where o.id = ?1 " +
            " and current_timestamp between b.start and b.end " +
            " order by b.start desc "
    )
    List<Booking> findByOwnerCurrentBookingsOrderByStartDesc(Integer ownerId, Pageable pageable);

    @Query(" select b from Booking b join b.item as i join i.owner o " +
            " where o.id = ?1 " +
            " and b.end < current_timestamp " +
            " order by b.start desc "
    )
    List<Booking> findByOwnerPast(Integer ownerId, Pageable pageable);

    @Query(" select b from Booking b join b.item as i join i.owner o " +
            " where o.id = ?1 " +
            " and b.start > current_timestamp " +
            " order by b.start desc "
    )
    List<Booking> findByOwnerFuture(Integer ownerId, Pageable pageable);

    @Query(value =
            "select b.id, b.BOOKER_ID as bookerId " +
                    " from ITEM t " +
                    " join BOOKING b on t.ID = b.ITEM_ID " +
                    "  and b.START_DATE <= now() " +
                    "where t.id = ?1 " +
                    "order by b.START_DATE desc " +
                    "limit 1",
            nativeQuery = true)
    ShortBookingView findLastItemBooking(Integer itemId);

    @Query(value =
            "select b.id, b.BOOKER_ID as bookerId " +
                    " from ITEM t " +
                    " join BOOKING b on t.ID = b.ITEM_ID " +
                    "  and b.START_DATE > now() " +
                    "where t.id = ?1 " +
                    "order by b.START_DATE " +
                    "limit 1",
            nativeQuery = true)
    ShortBookingView findNextItemBooking(Integer itemId);
}
