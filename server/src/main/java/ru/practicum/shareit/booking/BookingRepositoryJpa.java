package ru.practicum.shareit.booking;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.Status;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface BookingRepositoryJpa extends JpaRepository<Booking, Integer> {

    List<Booking> findAllByBookerId(Integer bookerId, Pageable pageable);

    List<Booking> findByBookerIdAndStatus(Integer bookerId, String status, Pageable pageable);

    List<Booking> findByBookerIdAndStatus(Integer bookerId, Status status, Pageable pageable);

    @Query("select bo " +
            "from Booking bo " +
            "where bo.booker.id = ?1 and bo.start < ?2 and bo.end > ?2 ")
    List<Booking> findByBookerIdCurrent(Integer bookerId, LocalDateTime now, Pageable pageable);

    @Query(value = "select bo " +
            "from Booking as bo " +
            "where bo.booker.id = ?1 and  bo.end > ?2 ")
    List<Booking> findByBookerIdFuture(Integer bookerId, LocalDateTime localDateTime, Pageable pageable);

    List<Booking> findByBookerIdAndEndBefore(Integer bookerId, LocalDateTime now, Pageable pageable);

    @Query("SELECT b FROM Booking b JOIN b.item i JOIN b.booker u WHERE i.owner.id = ?1 ")
    List<Booking> findByItemOwnerOrderByStartDesc(Integer idUser, Pageable pageable);

    List<Booking> findAllByItemOwnerIdAndStatus(Integer bookerId, String status, Pageable pageable);

    List<Booking> findAllByItemOwnerIdAndStatus(Integer bookerId, Status status, Pageable pageable);


    @Query("SELECT b FROM Booking b JOIN b.item i JOIN b.booker u WHERE i.owner.id = ?1 AND b.end > ?2")
    List<Booking> findByItemOwnerFuture(Integer bookerId, LocalDateTime localDateTime, Pageable pageable);

    @Query("SELECT b FROM Booking b JOIN b.item i JOIN b.booker u WHERE i.owner.id = ?1 AND b.end < ?2")
    List<Booking> findByItemOwnerPaste(Integer bookerId, LocalDateTime localDateTime, Pageable pageable);

    @Query("SELECT b FROM Booking b JOIN b.item i JOIN b.booker u WHERE i.owner.id = ?1 AND b.start < ?2 AND b.end > ?2")
    List<Booking> findByItemOwnerCurrent(Integer bookerId, LocalDateTime localDateTime, Pageable pageable);

    Booking findFirstByItemIdAndEndBefore(Integer itemId, LocalDateTime localDateTime, Sort sort);

    Booking findFirstByItemIdAndStartBefore(Integer itemId, LocalDateTime localDateTime, Sort sort);

    Booking findFirstByItemIdAndStartAfter(Integer itemId, LocalDateTime localDateTime, Sort sort);

    Booking findFirstByItemIdAndStartAfterAndStatus(Integer itemId, LocalDateTime localDateTime, Status status, Sort sort);

    Optional<Booking> findFirstByBookerIdAndItemIdAndEndBefore(Integer bookerId, Integer itemId, LocalDateTime now);
}
