package ru.practicum.shareit.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserMapper;

import java.util.*;


@Service
public class UserServiceImpl implements UserService {
    private int generationId = 1;

    @Autowired
    private UserRepository userRepository;
    private final Map<Integer, String> emailUniqMap = new HashMap();


    public UserDto create(User user) {
        validate(user);
        user.setId(generationId++);
        return UserMapper.toUserDto(userRepository.create(user));

    }

    public UserDto getUser(int id) {
        return UserMapper.toUserDto(userRepository.getUser(id));
    }

    public UserDto update(User user, int id) {
        User user1 = userRepository.update(user, id);
        validateUpdate(user1, id);
        userRepository.putUpdate(user1);
        return UserMapper.toUserDto(user1);
    }

    public Collection<UserDto> getAll() {
        return userRepository.getAll();
    }

    public void delete(int id) {
        userRepository.delete(id);
        emailUniqMap.put(id, null);
    }

    public void validate(User user) {
        if (emailUniqMap.containsValue(user.getEmail())) {
            throw new ValidationException("Этот Email занят");
        } else {
            emailUniqMap.put(generationId, user.getEmail());
        }
    }

    public void validateUpdate(User user, int id) {
        for (int i = 1; i <= emailUniqMap.size(); i++) {
            String email = emailUniqMap.get(i);
            if ((user.getEmail().equals(email) && (i != id))) {
                throw new ValidationException("Этот Email занят");
            }
        }
        emailUniqMap.put(id, user.getEmail());
    }
}
