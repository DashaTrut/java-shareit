package ru.practicum.shareit.booking.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
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

import static ru.practicum.shareit.booking.model.Status.WAITING;

@Service
@RequiredArgsConstructor
public class BookingServiceImpl {
    private final ItemRepositoryJpa itemRepositoryJpa;
    private final UserRepositoryJpa userRepositoryJpa;
    private final BookingRepositoryJpa bookingRepositoryJpa;

    public Booking create(BookingDto bookingDto, Integer id) {
        if (id != null) {
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
        } else {
            throw new EntityNotFoundException("не передали id");
        }
    }

    public Booking updateStatusBooking(Integer idBooking, Status status, Integer idUser) {
        if (idUser != null) {
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
        } else {
            throw new EntityNotFoundException("не передали id");
        }

    }

    public Booking getBooking(Integer bookingId, Integer idUser) {
        Booking booking = bookingRepositoryJpa.findById(bookingId).orElseThrow(() ->
                new EntityNotFoundException("Бронирования не существует"));
        if (idUser == booking.getBooker().getId() || idUser == booking.getItem().getOwner().getId()) {
            return booking;
        } else {
            throw new BookingNotFoundException("Не суйте нос в чужие дела");
        }
    }

    public Collection<Booking> getBookingForState(Integer idUser, String string) {
        State state = chekState(string);
        User user = userRepositoryJpa.findById(idUser).orElseThrow(() ->
                new EntityNotFoundException("Пользователя не существует"));
        switch (state) {
            case ALL:
                return bookingRepositoryJpa.findByBookerIdOrderByStartDesc(idUser);
            case WAITING:
                return bookingRepositoryJpa.findByBookerIdAndStatusOrderByStartDesc(idUser, WAITING);
            case REJECTED:
                return bookingRepositoryJpa.findByBookerIdAndStatusOrderByStartDesc(idUser, Status.REJECTED);
            case CURRENT:
                return bookingRepositoryJpa.findByBookerIdCurrent(idUser, LocalDateTime.now());
            case FUTURE:
                return bookingRepositoryJpa.findByBookerIdFuture(idUser, LocalDateTime.now());
            case PAST:
                return bookingRepositoryJpa.findByBookerIdAndEndBeforeOrderByStartDesc(idUser, LocalDateTime.now());
            default:
                throw new BookingException("У пользователя нет бронирований");
        }
    }

    public Collection<Booking> getBookingForOwnerAndState(Integer idUser, String string) {
        State state = chekState(string);
        User user = userRepositoryJpa.findById(idUser).orElseThrow(() ->
                new EntityNotFoundException("Пользователя не существует"));
        Collection<Item> listItem = itemRepositoryJpa.findByOwnerId(idUser);
        if (listItem.isEmpty()) {
            throw new BookingException("У пользователя нет вещей");
        }
        switch (state) {
            case ALL:
                return bookingRepositoryJpa.findByItemOwnerOrderByStartDesc(idUser);
            case WAITING:
                return bookingRepositoryJpa.findAllByItemOwnerIdAndStatusOrderByStartDesc(idUser, Status.WAITING);
            case REJECTED:
                return bookingRepositoryJpa.findAllByItemOwnerIdAndStatusOrderByStartDesc(idUser, Status.REJECTED);
            case CURRENT:
                return bookingRepositoryJpa.findByItemOwnerCurrent(idUser, LocalDateTime.now());
            case FUTURE:
                return bookingRepositoryJpa.findByItemOwnerFuture(idUser, LocalDateTime.now());
            case PAST:
                return bookingRepositoryJpa.findByItemOwnerPaste(idUser, LocalDateTime.now());
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
