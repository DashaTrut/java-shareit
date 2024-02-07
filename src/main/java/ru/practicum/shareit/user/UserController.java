package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.UserDto;

import javax.validation.Valid;
import java.util.Collection;

@Slf4j
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
    public UserDto addUser(@Valid @RequestBody UserDto user) {
        log.info("Add user{}", user);
        return userServiceImpl.create(user);
    }


    @PatchMapping("{id}") //обновление пользователя;
    public UserDto updateUser(@RequestBody UserDto user, @PathVariable int id) {
        log.info("Update user{}", user);
        return userServiceImpl.update(user, id);
    }

    @GetMapping("{id}")
    public UserDto getForId(@PathVariable int id) {
        log.info("Get user id{}", id);
        return userServiceImpl.getUser(id);
    }

    @DeleteMapping("{id}")
    public void deleteUser(@PathVariable int id) {
        log.info("Delete user id{}", id);
        userServiceImpl.delete(id);
    }
}