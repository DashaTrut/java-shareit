package ru.practicum.shareit.booking;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.Status;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Optional;

public interface BookingRepositoryJpa extends JpaRepository<Booking, Integer> {
    public Collection<Booking> findByBookerIdOrderByStartDesc(Integer bookerId);

    public Collection<Booking> findByBookerIdAndStatusOrderByStartDesc(Integer bookerId, Status status);

    @Query(value = "select * " +
            "from booking as bo join users as us on bo.booker_id = us.id join items as it on bo.item_id = it.id  " +
            "where bo.booker_id = ?1 and bo.start_time < ?2 and  bo.end_time > ?2 " +
            "ORDER BY bo.start_time DESC", nativeQuery = true)
    Collection<Booking> findByBookerIdCurrent(Integer bookerId, LocalDateTime now);


    @Query(value = "select * " +
            "from booking as bo join users as us on bo.booker_id = us.id join items as it on bo.item_id = it.id  " +
            "where bo.booker_id = ?1 and  bo.end_time > ?2 " +
            "ORDER BY bo.start_time DESC", nativeQuery = true)
    Collection<Booking> findByBookerIdFuture(Integer bookerId, LocalDateTime localDateTime);

    public Collection<Booking> findByBookerIdAndEndBeforeOrderByStartDesc(Integer bookerId, LocalDateTime now);


    @Query(value = "select * " +
            "from booking as bo join users as us on bo.booker_id = us.id join items as it on bo.item_id = it.id  " +
            "where it.user_owner = ? " +
            "ORDER BY bo.start_time DESC", nativeQuery = true)
    Collection<Booking> findByItemOwnerOrderByStartDesc(Integer idUser);

    @Query(value = "select bo.id, bo.start_time, bo.end_time, it.id, us.id, bo.status, " +
            "it.name, it.user_owner, it.description, it.available, it.request, " +
            "us.name, us.email " +
            "from booking as bo join users as us on bo.booker_id = us.id join items as it on bo.item_id = it.id  " +
            "where it.user_owner = ?1 and bo.status = ?2 " +
            "ORDER BY bo.start_time DESC ", nativeQuery = true)
    Collection<Booking> findByItemOwnerAndStatusOrderByStartDesc(Integer idUser, Status status);


    public Collection<Booking> findAllByItemOwnerIdAndStatusOrderByStartDesc(Integer bookerId, Status status);

    @Query(value = "select * " +
            "from booking as bo join users as us on bo.booker_id = us.id join items as it on bo.item_id = it.id  " +
            "where it.user_owner = ?1 and  bo.end_time > ?2 " +
            "ORDER BY bo.start_time DESC", nativeQuery = true)
    Collection<Booking> findByItemOwnerFuture(Integer bookerId, LocalDateTime localDateTime);

    @Query(value = "select * " +
            "from booking as bo join users as us on bo.booker_id = us.id join items as it on bo.item_id = it.id  " +
            "where it.user_owner = ?1 and  bo.end_time < ?2 " +
            "ORDER BY bo.start_time DESC", nativeQuery = true)
    Collection<Booking> findByItemOwnerPaste(Integer bookerId, LocalDateTime localDateTime);

    @Query(value = "select * " +
            "from booking as bo join users as us on bo.booker_id = us.id join items as it on bo.item_id = it.id  " +
            "where it.user_owner = ?1 and  bo.start_time < ?2 and bo.end_time > ?2 " +
            "ORDER BY bo.start_time DESC", nativeQuery = true)
    Collection<Booking> findByItemOwnerCurrent(Integer bookerId, LocalDateTime localDateTime);

    @Query(value = "select * " +
            "from booking as bo join users as us on bo.booker_id = us.id join items as it on bo.item_id = it.id  " +
            "where it.id = ?1 and  bo.end_time < ?2 " +
            "ORDER BY bo.start_time DESC " +
            "limit 1", nativeQuery = true)
    Booking findByItemIdPaste(Integer itemId, LocalDateTime localDateTime);

    public Booking findFirstByItemIdAndStartBeforeOrderByEndDesc(Integer itemId, LocalDateTime localDateTime);

    @Query(value = "select * " +
            "from booking as bo join users as us on bo.booker_id = us.id join items as it on bo.item_id = it.id  " +
            "where it.id = ?1 and  bo.start_time > ?2 " +
            "ORDER BY bo.start_time ASC " +
            "limit 1", nativeQuery = true)
    Booking findByItemIdFuture(Integer itemId, LocalDateTime localDateTime);

    public Booking findFirstByItemIdAndStartAfterAndStatusOrderByStartAsc(Integer itemId, LocalDateTime localDateTime, Status status);


    public Optional<Booking> findFirstByBookerIdAndItemIdAndEndBefore(Integer bookerId, Integer itemId, LocalDateTime now);
}
