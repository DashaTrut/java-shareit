package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.exception.EntityNotFoundException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserMapper;
import ru.practicum.shareit.user.repository.UserRepositoryJpa;

import java.util.*;


@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepositoryJpa userRepositoryJpa;

    @Transactional()
    public User create(User user) {
        try {
            return userRepositoryJpa.save(user);
        } catch (Exception e) {
            throw new ValidationException("Ошибка валидации");
        }
    }

    @Transactional()
    public User getUser(int id) {
        return userRepositoryJpa.findById(id).orElseThrow(() ->
                new EntityNotFoundException("Пользователя не существует"));

    }

    @Transactional
    public UserDto update(UserDto userDto, int id) {
        User oldUser = getUser(id);
        if (userDto.getName() != null) {
            oldUser.setName(userDto.getName());
        }
        if (userDto.getEmail() != null) {
            if (!userDto.getEmail().equals(oldUser.getEmail())) {
                oldUser.setEmail(userDto.getEmail());
            }
        }
        try {
            userRepositoryJpa.save(oldUser);
        } catch (Exception e) {
            throw new ValidationException("Ошибка валидации");
        }
        return UserMapper.toUserDto(oldUser);
    }

    public List<UserDto> getAll() {
        return UserMapper.mapToUserDto(userRepositoryJpa.findAll());
    }

    @Transactional
    public void delete(int id) {
        User user = getUser(id);
        userRepositoryJpa.delete(user);
    }

}


