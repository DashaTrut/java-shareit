package ru.practicum.shareit.user.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.exception.EntityNotFoundException;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.repository.UserRepositoryJpa;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {
    @Mock
    private UserRepositoryJpa userRepositoryJpa;

    @InjectMocks
    private UserServiceImpl userService;

    @Captor
    private ArgumentCaptor<User> userArgumentCaptor;

    @Test
    void getUser_whenUserFound_thenReturnedUserDto() {
        int userId = 0;
        User expectedUser = new User();
        when(userRepositoryJpa.findById(userId)).thenReturn(Optional.of(expectedUser));

        User actualUser = userService.getUser(userId);

        assertEquals(expectedUser, actualUser);
    }

    @Test
    void getUser_whenUserNotFound_thenReturnedUserDto() {
        int userId = 0;
        when(userRepositoryJpa.findById(userId)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> userService.getUser(userId));
    }

    @Test
    void createUser_whenUserName_thenSavedUser() {
        User userToSave = new User();

        when(userRepositoryJpa.save(userToSave)).thenReturn(userToSave);
        User actualUser = userService.create(userToSave);

        assertEquals(userToSave, actualUser);
        verify(userRepositoryJpa).save(userToSave);
    }


    @Test
    void updateUser_whenUserFound_thenUpdateEmailAndName() {
        int userId = 0;
        User oldUser = new User();
        oldUser.setName("first name");
        oldUser.setEmail("set@email.com");

        UserDto newUser = new UserDto();
        oldUser.setName("first first name");
        oldUser.setEmail("test@email.com");
        when(userRepositoryJpa.findById(userId)).thenReturn(Optional.of(oldUser));

        UserDto userActual = userService.update(newUser, userId);


        verify(userRepositoryJpa).save(userArgumentCaptor.capture());
        User savedUser = userArgumentCaptor.getValue();

        assertEquals("first first name", savedUser.getName());
    }

    @Test
    void updateUser_whenUserFound_thenUpdateThrown() {
        int userId = 0;
        User oldUser = new User();
        oldUser.setName("first name");
        oldUser.setEmail("set@email.com");

        UserDto newUser = new UserDto();
        oldUser.setName("first first name");
        oldUser.setEmail("test@email.com");
        when(userRepositoryJpa.findById(userId)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class,
                () -> userService.update(newUser, userId));

        verify(userRepositoryJpa, never()).save(new User());
        verify(userRepositoryJpa, times(0)).save(new User());
    }

    @Test
    void getAllUser_ListUserDto() {
        UserDto oldUserDto = new UserDto();
        oldUserDto.setName("first name");
        oldUserDto.setEmail("set@email.com");

        UserDto newUserDto = new UserDto();
        newUserDto.setName("first first name");
        newUserDto.setEmail("test@email.com");
        User oldUser = new User();
        oldUser.setName("first name");
        oldUser.setEmail("set@email.com");

        User newUser = new User();
        oldUser.setName("first first name");
        oldUser.setEmail("test@email.com");
        List<User> list = List.of(oldUser, newUser);
        when(userRepositoryJpa.findAll()).thenReturn(list);

        List<UserDto> listResult = userService.getAll();

        verify(userRepositoryJpa, times(1)).findAll();
        assertNotNull(listResult);
    }
}