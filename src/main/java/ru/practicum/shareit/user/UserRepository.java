package ru.practicum.shareit.user;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.exception.EntityNotFoundException;
import ru.practicum.shareit.exception.ValidationException;


import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Component
public class UserRepository {
    private final Map<Integer, User> storageUser = new HashMap<>();
    private int generationId = 0;

    public User create(User user) {
        validate(user);
        user.setId(++generationId);
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
        validateUpdate(user, id);
        storageUser.put(user.getId(), user);
        return user;
    }

    public Collection<User> getAll() {
        ArrayList<User> list = new ArrayList<>();
        if (storageUser.isEmpty()) {
            return list;
        }
        for (User user : storageUser.values()) {
            list.add(user);
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

    public void validate(User user) {
        for (User userMap : storageUser.values())
            if (user.getEmail().equals(userMap.getEmail())) {
                throw new ValidationException("Этот Email занят");
            }
        ;
    }

    public void validateUpdate(User user, int id) {
        for (User userMap : storageUser.values())
            if ((user.getEmail().equals(userMap.getEmail()) && (userMap.getId() != id))) {
                throw new ValidationException("Этот Email занят");
            }
        ;
    }
}


