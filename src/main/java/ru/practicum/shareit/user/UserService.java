package ru.practicum.shareit.user;

import java.util.Collection;

public interface UserService {
    public User create(User user);

    public User getUser(int id);

    public User update(User user, int id);


    public Collection<User> getAll();

    public void delete(int id);

}
