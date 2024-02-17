package ru.practicum.shareit.booking.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.BookingRepositoryJpa;
import ru.practicum.shareit.booking.model.State;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingMapper;
import ru.practicum.shareit.exception.BookingException;
import ru.practicum.shareit.exception.BookingNotFoundException;
import ru.practicum.shareit.exception.EntityNotFoundException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepositoryJpa;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.repository.UserRepositoryJpa;

import java.time.LocalDateTime;
import java.util.Collection;

import static ru.practicum.shareit.booking.model.Status.REJECTED;
import static ru.practicum.shareit.booking.model.Status.WAITING;

@Service
@RequiredArgsConstructor
public class BookingServiceImpl {
    private final ItemRepositoryJpa itemRepositoryJpa;
    private final UserRepositoryJpa userRepositoryJpa;
    private final BookingRepositoryJpa bookingRepositoryJpa;
    private Sort start = Sort.by(Sort.Direction.DESC, "start");

    @Transactional(readOnly = true)
    public Booking create(BookingDto bookingDto, int id) {
        User user = userRepositoryJpa.findById(id).orElseThrow(() ->
                new EntityNotFoundException("Пользователя не существует"));
        Item item = itemRepositoryJpa.findById(bookingDto.getItemId()).orElseThrow(() ->
                new EntityNotFoundException("Вещи не существует"));
        if (item.getOwner().getId() == id) {
            throw new EntityNotFoundException("нельзя бронировать свою вещь");
        }
        if (item.getAvailable() == false || bookingDto.getStart().isAfter(bookingDto.getEnd()) ||
                bookingDto.getStart().isEqual(bookingDto.getEnd())) {
            throw new BookingException("Бронирование вещи невозможно");
        }
        Booking booking = BookingMapper.toBooking(bookingDto, user, item);
        Booking result = bookingRepositoryJpa.save(booking);
        return result;
    }

    @Transactional
    public Booking updateStatusBooking(int idBooking, Boolean approved, int idUser) {
        Status status;
        if (approved) {
            status = Status.APPROVED;
        } else {
            status = Status.REJECTED;
        }
        Booking booking = bookingRepositoryJpa.findById(idBooking).orElseThrow(() ->
                new EntityNotFoundException("Бронирования не существует"));
        if (booking.getStatus() == status) {
            throw new BookingException("Вы уже изменили статус");
        }
        Item item = itemRepositoryJpa.findById(booking.getItem().getId()).orElseThrow(() ->
                new EntityNotFoundException("Вещи не существует"));
        User user = userRepositoryJpa.findById(idUser).orElseThrow(() ->
                new EntityNotFoundException("Пользователя не существует"));
        if (item.getOwner().getId() == idUser) {
            booking.setStatus(status);
            return bookingRepositoryJpa.save(booking);
        } else {
            throw new EntityNotFoundException("Только владелец может подтвердить бронирование вещи");
        }
    }

    @Transactional(readOnly = true)
    public Booking getBooking(int bookingId, int idUser) {
        Booking booking = bookingRepositoryJpa.findById(bookingId).orElseThrow(() ->
                new EntityNotFoundException("Бронирования не существует"));
        if (idUser == booking.getBooker().getId() || idUser == booking.getItem().getOwner().getId()) {
            return booking;
        } else {
            throw new BookingNotFoundException("Не суйте нос в чужие дела");
        }
    }

    @Transactional(readOnly = true)
    public Collection<Booking> getBookingForState(int idUser, String string) {
        State state = chekState(string);
        User user = userRepositoryJpa.findById(idUser).orElseThrow(() ->
                new EntityNotFoundException("Пользователя не существует"));
        switch (state) {
            case ALL:
                return bookingRepositoryJpa.findByBookerIdOrderByStartDesc(idUser, Sort.by(Sort.Direction.DESC, "start"));
            case WAITING:
                return bookingRepositoryJpa.findByBookerIdAndStatus(idUser, WAITING, start);
            case REJECTED:
                return bookingRepositoryJpa.findByBookerIdAndStatus(idUser, REJECTED, start);
            case CURRENT:
                return bookingRepositoryJpa.findByBookerIdCurrent(idUser, LocalDateTime.now(), start);
            case FUTURE:
                return bookingRepositoryJpa.findByBookerIdFuture(idUser, LocalDateTime.now(), start);
            case PAST:
                return bookingRepositoryJpa.findByBookerIdAndEndBefore(idUser, LocalDateTime.now(), start);
            default:
                throw new BookingException("У пользователя нет бронирований");
        }
    }

    @Transactional(readOnly = true)
    public Collection<Booking> getBookingForOwnerAndState(int idUser, String string) {
        State state = chekState(string);
        User user = userRepositoryJpa.findById(idUser).orElseThrow(() ->
                new EntityNotFoundException("Пользователя не существует"));
        Collection<Item> listItem = itemRepositoryJpa.findByOwnerId(idUser);
        if (listItem.isEmpty()) {
            throw new BookingException("У пользователя нет вещей");
        }
        switch (state) {
            case ALL:
                return bookingRepositoryJpa.findByItemOwnerOrderByStartDesc(idUser, start);
            case WAITING:
                return bookingRepositoryJpa.findAllByItemOwnerIdAndStatus(idUser, WAITING, start);
            case REJECTED:
                return bookingRepositoryJpa.findAllByItemOwnerIdAndStatus(idUser, REJECTED, start);
            case CURRENT:
                return bookingRepositoryJpa.findByItemOwnerCurrent(idUser, LocalDateTime.now(), start);
            case FUTURE:
                return bookingRepositoryJpa.findByItemOwnerFuture(idUser, LocalDateTime.now(), start);
            case PAST:
                return bookingRepositoryJpa.findByItemOwnerPaste(idUser, LocalDateTime.now(), start);
            default:
                throw new BookingException("У пользователя нет вещей");
        }
    }

    private State chekState(String stringState) {
        try {
            return State.valueOf(stringState);
        } catch (IllegalArgumentException e) {
            throw new ValidationException("Unknown state: UNSUPPORTED_STATUS");
        }
    }
}
