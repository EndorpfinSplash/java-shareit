package ru.practicum.shareit.booking;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.practicum.shareit.booking.dto.BookingCreationDTO;
import ru.practicum.shareit.booking.dto.BookingOutputDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;

import java.time.LocalDateTime;

class BookingMapperTest {
    final User originTestUser = User.builder()
            .id(2)
            .name("Test User")
            .build();
    final Item originTestItem = Item.builder()
            .id(3)
            .name("Test Item")
            .build();

    @Test
    void toBooking() {

        BookingCreationDTO originBookingCreationDTO = BookingCreationDTO.builder()
                .start(LocalDateTime.now())
                .end(LocalDateTime.now().plusDays(2))
                .itemId(5)
                .build();
        Booking resultBooking = BookingMapper.toBooking(originBookingCreationDTO, originTestUser, originTestItem);
        Assertions.assertNotNull(resultBooking);
        Assertions.assertEquals(originBookingCreationDTO.getStart(), resultBooking.getStart());
        Assertions.assertEquals(originBookingCreationDTO.getEnd(), resultBooking.getEnd());
        Assertions.assertEquals(originTestItem, resultBooking.getItem());
        Assertions.assertEquals(originTestItem.getId(), resultBooking.getItem().getId());
        Assertions.assertEquals(originTestItem.getName(), resultBooking.getItem().getName());
        Assertions.assertEquals(originTestUser, resultBooking.getBooker());
        Assertions.assertEquals(BookingStatus.WAITING, resultBooking.getBookingStatus());
    }

    @Test
    void toOutBookingDto() {

        Booking originalBooking = Booking.builder()
                .id(1)
                .booker(originTestUser)
                .bookingStatus(BookingStatus.WAITING)
                .start(LocalDateTime.now())
                .end(LocalDateTime.now().plusDays(2))
                .item(originTestItem)
                .build();
        BookingOutputDto reslutBookingOutputDto = BookingMapper.toOutBookingDto(originalBooking);

        Assertions.assertNotNull(reslutBookingOutputDto);
        Assertions.assertEquals(originalBooking.getId(), reslutBookingOutputDto.getId());
        Assertions.assertEquals(originalBooking.getBooker(), reslutBookingOutputDto.getBooker());
        Assertions.assertEquals(originalBooking.getItem(), reslutBookingOutputDto.getItem());
        Assertions.assertEquals(originalBooking.getStart(), reslutBookingOutputDto.getStart());
        Assertions.assertEquals(originalBooking.getEnd(), reslutBookingOutputDto.getEnd());
        Assertions.assertEquals(originalBooking.getItem().getId(), reslutBookingOutputDto.getItem().getId());
        Assertions.assertEquals(originalBooking.getItem().getName(), reslutBookingOutputDto.getItem().getName());

    }
}