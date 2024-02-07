package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserMapper;

import java.util.*;


@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private int generationId = 1;
    private final UserRepository userRepository;
    private final Set emailUniqSet = new HashSet<>();

    public UserDto create(UserDto userDto) {
        validate(userDto.getEmail());
        userDto.setId(generationId++);
        return UserMapper.toUserDto(userRepository.create(UserMapper.toUser(userDto)));
    }

    public UserDto getUser(int id) {
        return UserMapper.toUserDto(userRepository.getUser(id));
    }

    public UserDto update(UserDto userDto, int id) {
        User oldUser = userRepository.getUser(id);
        if (userDto.getName() != null) {
            oldUser.setName(userDto.getName());
        }
        if (userDto.getEmail() != null) {
            if (!userDto.getEmail().equals(oldUser.getEmail())) {
                validate(userDto.getEmail());
                emailUniqSet.remove(oldUser.getEmail());
                oldUser.setEmail(userDto.getEmail());
            }
        }
        userRepository.update((oldUser));
        return UserMapper.toUserDto(oldUser);
    }

    public Collection<UserDto> getAll() {
        return userRepository.getAll();
    }

    public void delete(int id) {
        User user = userRepository.getUser(id);
        userRepository.delete(id);
        emailUniqSet.remove(user.getEmail());
    }

    public void validate(String email) {
        if (emailUniqSet.contains(email)) {
            throw new ValidationException("Этот Email занят");
        } else {
            emailUniqSet.add(email);
        }
    }
}


