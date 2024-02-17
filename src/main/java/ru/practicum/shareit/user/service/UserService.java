package ru.practicum.shareit.user.service;

import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.dto.UserDto;

import java.util.List;

public interface UserService {
    User create(User user);

    User getUser(int id);

    UserDto update(UserDto userDto, int id);

    List<User> getAll();

    void delete(int id);

}
