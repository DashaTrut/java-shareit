package ru.practicum.shareit.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;


@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;

    public User create(User user) {
        return userRepository.create(user);
    }

    public User getUser(int id) {
        return userRepository.getUser(id);
    }

    public User update(User user, int id) {
        return userRepository.update(user, id);
    }


    public Collection<User> getAll() {
        return userRepository.getAll();
    }

    public void delete(int id) {
        userRepository.delete(id);
    }


}
