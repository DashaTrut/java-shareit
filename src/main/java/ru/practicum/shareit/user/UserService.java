package ru.practicum.shareit.user;

import ru.practicum.shareit.user.dto.UserDto;

import java.util.Collection;

public interface UserService {
    UserDto create(UserDto userDto);

    UserDto getUser(int id);

    UserDto update(UserDto userDto, int id);

    Collection<UserDto> getAll();

    void delete(int id);

}
