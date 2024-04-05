package ru.practicum.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.user.dto.UserDto;

import javax.validation.Valid;

@Slf4j
@Validated
@Controller
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserClient userClient;

    @GetMapping
    public ResponseEntity<Object> getUsers() {  //    получение списка всех пользователей.
        return userClient.getAll();
    }

    @PostMapping //добавление пользователя;
    public ResponseEntity<Object> addUser(@Valid @RequestBody UserDto user) {
        log.info("Add user{}", user);
        return userClient.create(user);
    }


    @PatchMapping("{id}") //обновление пользователя;
    public ResponseEntity<Object> updateUser(@RequestBody UserDto user, @PathVariable int id) {
        log.info("Update user{}", user);
        return userClient.update(user, id);
    }

    @GetMapping("{id}")
    public ResponseEntity<Object> getForId(@PathVariable int id) {
        log.info("Get user id{}", id);
        return userClient.getUser(id);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Object> deleteUser(@PathVariable int id) {
        log.info("Delete user id{}", id);
        return userClient.delete(id);
    }
}