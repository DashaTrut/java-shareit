package ru.practicum.shareit.user.service;

import org.hibernate.exception.SQLGrammarException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.exception.EntityNotFoundException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.repository.UserRepositoryJpa;

import java.util.Collections;
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
        User userToSave = new User(1, "owner@email.com", "nameUser");

        when(userRepositoryJpa.save(userToSave)).thenReturn(userToSave);
        User actualUser = userService.create(userToSave);

        assertEquals(userToSave, actualUser);
        verify(userRepositoryJpa).save(userToSave);
    }

    @Test
    void createUser_whenUserName_thenThrown() {
        User userToSave = new User(1, "owner@email.com", "nameUser");

        when(userRepositoryJpa.save(userToSave)).thenThrow(ValidationException.class);
        assertThrows(ValidationException.class, () -> userService.create(userToSave));
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
        assertEquals("test@email.com", savedUser.getEmail());
    }

    @Test
    void updateUser_whenUserFound_thenUpdateEmail() {
        int userId = 0;
        User oldUser = new User();
        oldUser.setName("first name");
        oldUser.setEmail("set@email.com");

        UserDto newUser = new UserDto();
        oldUser.setEmail("test@email.com");
        when(userRepositoryJpa.findById(userId)).thenReturn(Optional.of(oldUser));

        UserDto userActual = userService.update(newUser, userId);

        verify(userRepositoryJpa).save(userArgumentCaptor.capture());
        User savedUser = userArgumentCaptor.getValue();

        assertEquals("first name", savedUser.getName());
        assertEquals("test@email.com", savedUser.getEmail());
    }


    @Test
    void updateUser_whenUserFound_thenUpdateName() {
        int userId = 0;
        User oldUser = new User();
        oldUser.setName("first name");
        oldUser.setEmail("set@email.com");

        UserDto newUser = new UserDto();
        oldUser.setName("first first name");
        when(userRepositoryJpa.findById(userId)).thenReturn(Optional.of(oldUser));

        UserDto userActual = userService.update(newUser, userId);


        verify(userRepositoryJpa).save(userArgumentCaptor.capture());
        User savedUser = userArgumentCaptor.getValue();

        assertEquals("first first name", savedUser.getName());
        assertEquals("set@email.com", savedUser.getEmail());
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
    void updateUser_whenUserFound_thenThrowSave() {
        int userId = 0;
        User oldUser = new User();
        oldUser.setName("first name");
        oldUser.setEmail("set@email.com");

        UserDto newUser = new UserDto();
        oldUser.setName("first first name");
        when(userRepositoryJpa.findById(userId)).thenReturn(Optional.of(oldUser));
        when(userRepositoryJpa.save(any(User.class))).thenThrow(ValidationException.class);

        assertThrows(ValidationException.class, () -> userService.update(newUser, userId));
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

    @Test
    void getAllUser_empty() {
        when(userRepositoryJpa.findAll()).thenReturn(Collections.EMPTY_LIST);

        List<UserDto> listResult = userService.getAll();

        verify(userRepositoryJpa, times(1)).findAll();
        assertEquals(listResult.size(), 0);
    }

    @Test
    void getAllUser_Throw() {
        when(userRepositoryJpa.findAll()).thenThrow(SQLGrammarException.class);

        assertThrows(SQLGrammarException.class, () -> userService.getAll());
    }

    @Test
    void delete_Throw() {
        when(userRepositoryJpa.findById(1)).thenThrow(EntityNotFoundException.class);

        assertThrows(EntityNotFoundException.class, () -> userService.delete(1));
    }

    @Test
    void delete() {
        int userId = 0;
        User oldUser = new User();
        oldUser.setName("first name");
        oldUser.setEmail("set@email.com");

        when(userRepositoryJpa.findById(userId)).thenReturn(Optional.of(oldUser));

        userService.delete(userId);
        verify(userRepositoryJpa, times(1)).delete(any(User.class));
    }
}