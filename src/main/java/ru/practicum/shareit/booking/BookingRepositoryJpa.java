package ru.practicum.shareit.booking;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.Status;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Optional;

public interface BookingRepositoryJpa extends JpaRepository<Booking, Integer> {
    public Collection<Booking> findByBookerIdOrderByStartDesc(Integer bookerId, Sort sort);

    public Collection<Booking> findByBookerIdAndStatus(Integer bookerId, Status status, Sort sort);

    @Query("select bo " +
            "from Booking bo " +
            "where bo.booker.id = ?1 and bo.start < ?2 and bo.end > ?2 ")
    Collection<Booking> findByBookerIdCurrent(Integer bookerId, LocalDateTime now, Sort sort);


    @Query(value = "select bo " +
            "from Booking as bo " +
            "where bo.booker.id = ?1 and  bo.end > ?2 ")
    Collection<Booking> findByBookerIdFuture(Integer bookerId, LocalDateTime localDateTime, Sort sort);

    public Collection<Booking> findByBookerIdAndEndBefore(Integer bookerId, LocalDateTime now, Sort sort);


    @Query("SELECT b FROM Booking b JOIN b.item i JOIN b.booker u WHERE i.owner.id = ?1 ")
    Collection<Booking> findByItemOwnerOrderByStartDesc(Integer idUser, Sort sort);

    public Collection<Booking> findAllByItemOwnerIdAndStatus(Integer bookerId, Status status, Sort sort);

    @Query("SELECT b FROM Booking b JOIN b.item i JOIN b.booker u WHERE i.owner.id = ?1 AND b.end > ?2")
    Collection<Booking> findByItemOwnerFuture(Integer bookerId, LocalDateTime localDateTime, Sort sort);

    @Query("SELECT b FROM Booking b JOIN b.item i JOIN b.booker u WHERE i.owner.id = ?1 AND b.end < ?2")
    Collection<Booking> findByItemOwnerPaste(Integer bookerId, LocalDateTime localDateTime, Sort sort);

    @Query("SELECT b FROM Booking b JOIN b.item i JOIN b.booker u WHERE i.owner.id = ?1 AND b.start < ?2 AND b.end > ?2")
    Collection<Booking> findByItemOwnerCurrent(Integer bookerId, LocalDateTime localDateTime, Sort sort);

    Booking findFirstByItemIdAndEndBefore(Integer itemId, LocalDateTime localDateTime, Sort sort);

    public Booking findFirstByItemIdAndStartBefore(Integer itemId, LocalDateTime localDateTime, Sort sort);

    Booking findFirstByItemIdAndStartAfter(Integer itemId, LocalDateTime localDateTime, Sort sort);

    public Booking findFirstByItemIdAndStartAfterAndStatus(Integer itemId, LocalDateTime localDateTime, Status status, Sort sort);

    public Optional<Booking> findFirstByBookerIdAndItemIdAndEndBefore(Integer bookerId, Integer itemId, LocalDateTime now);
}
