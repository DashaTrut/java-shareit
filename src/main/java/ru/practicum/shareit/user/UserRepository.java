package ru.practicum.shareit.user;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.exception.EntityNotFoundException;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserMapper;


import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Component
public class UserRepository {
    private final Map<Integer, User> storageUser = new HashMap<>();

    public User create(User user) {
        storageUser.put(user.getId(), user);
        return user;
    }


    public User update(User user, int id) {
        if (!storageUser.containsKey(id)) {
            throw new EntityNotFoundException(String.format("Обновление невозможно %s не сущесвует", user));
        }
        User oldUser = storageUser.get(id);
        if (user.getName() == null) {
            user.setName(oldUser.getName());
        }
        if (user.getEmail() == null) {
            user.setEmail(oldUser.getEmail());
        }
        user.setId(id);
        return user;
    }

    public void putUpdate(User user) {
        storageUser.put(user.getId(), user);
    }

    public Collection<UserDto> getAll() {
        ArrayList<UserDto> list = new ArrayList<>();
        if (storageUser.isEmpty()) {
            return list;
        }
        for (User user : storageUser.values()) {
            list.add(UserMapper.toUserDto(user));
        }
        return list;
    }


    public User getUser(int id) {
        if (storageUser.containsKey(id)) {
            return storageUser.get(id);
        }
        throw new EntityNotFoundException("Нет пользователя с таким id");
    }


    public void delete(int id) {
        if (storageUser.containsKey(id)) {
            storageUser.remove(id);
            return;
        }
        throw new EntityNotFoundException(String.format("Удаление невозможно %s не сущесвует", id));
    }
}


