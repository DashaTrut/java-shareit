package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Collection;

/**
 * TODO Sprint add-controllers.
 */
@Slf4j
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
    @Autowired
    private UserServiceImpl userServiceImpl;

    @GetMapping
    public Collection<User> getUsers() {  //    получение списка всех пользователей.
        return userServiceImpl.getAll();
    }

    @PostMapping //добавление пользователя;
    public User addUser(@Valid @RequestBody User user) {
        log.info("Add user{}", user);
        return userServiceImpl.create(user);
    }


    @PatchMapping("{id}") //обновление пользователя;
    public User updateUser(@RequestBody User user, @PathVariable int id) {
        log.info("Update user{}", user);
        return userServiceImpl.update(user, id);
    }

    @GetMapping("{id}")
    public User getForId(@PathVariable int id) {
        log.info("Get user id{}", id);
        return userServiceImpl.getUser(id);
    }

    @DeleteMapping("{id}")
    public void deleteUser(@PathVariable int id) {
        log.info("Delete user id{}", id);
        userServiceImpl.delete(id);
    }

}