package ru.practicum.shareit.user;

import ru.practicum.shareit.user.dto.UserDto;

import java.util.Collection;

public interface UserService {
    UserDto create(User user);

    UserDto getUser(int id);

    UserDto update(User user, int id);

    Collection<UserDto> getAll();

    void delete(int id);

}
