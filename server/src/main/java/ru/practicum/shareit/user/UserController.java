package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserMapper;
import ru.practicum.shareit.user.service.UserServiceImpl;

import java.util.Collection;

@Slf4j
@Validated
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserServiceImpl userServiceImpl;

    @GetMapping
    public Collection<UserDto> getUsers() {  //    получение списка всех пользователей.
        return userServiceImpl.getAll();
    }

    @PostMapping //добавление пользователя;
    public UserDto addUser(@RequestBody UserDto user) {
        log.info("Add user{}", user);
        return UserMapper.toUserDto(userServiceImpl.create(UserMapper.toUser(user)));
    }


    @PatchMapping("{id}") //обновление пользователя;
    public UserDto updateUser(@RequestBody UserDto user, @PathVariable int id) {
        log.info("Update user{}", user);
        return userServiceImpl.update(user, id);
    }

    @GetMapping("{id}")
    public UserDto getForId(@PathVariable int id) {
        log.info("Get user id{}", id);
        return UserMapper.toUserDto(userServiceImpl.getUser(id));
    }

    @DeleteMapping("{id}")
    public void deleteUser(@PathVariable int id) {
        log.info("Delete user id{}", id);
        userServiceImpl.delete(id);
    }
}