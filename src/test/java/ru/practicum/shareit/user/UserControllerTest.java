package ru.practicum.shareit.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.exception.EntityNotFoundException;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserMapper;
import ru.practicum.shareit.user.service.UserServiceImpl;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = UserController.class)
class UserControllerTest {
    @MockBean
    private UserServiceImpl userService;
    @Autowired
    private MockMvc mockMvc;
    private final ObjectMapper mapper = new ObjectMapper();

    @SneakyThrows
    @Test
    void getUsers() {
        int userId = 1;
        User user = new User(userId, "test@email.com", "lasa");
        when(userService.getUser(userId)).thenReturn(user);
        mockMvc.perform(get("/users/{id}", userId))
                .andExpect(status().isOk());

        verify(userService).getUser(userId);
    }

    @SneakyThrows
    @Test
    void getUsers_isThrow() {
        int userId = 1;
        User user = new User(userId, "test@email.com", "lasa");
        when(userService.getUser(userId)).thenThrow(EntityNotFoundException.class);
        mockMvc.perform(get("/users/{id}", userId))
                .andExpect(status().isNotFound());
    }

    @SneakyThrows
    @Test
    void addUser() {
        int userId = 1;
        User user = new User(userId, "test@email.com", "lasa");
        UserDto userDto = UserMapper.toUserDto(user);
        when(userService.create(user)).thenReturn(user);
        mockMvc.perform(post("/users")
                        .contentType("application/json")
                        .content(mapper.writeValueAsString(userDto)))
                .andExpect(status().isOk());

        verify(userService).create(user);
    }

    @SneakyThrows
    @Test
    void addUser_withValidationException() {
        int userId = 1;
        User user = new User(userId, null, "lasa");
        UserDto userDto = UserMapper.toUserDto(user);
        when(userService.create(user)).thenReturn(user);
        mockMvc.perform(post("/users")
                        .contentType("application/json")
                        .content(mapper.writeValueAsString(userDto)))
                .andExpect(status().isBadRequest());
    }

    @SneakyThrows
    @Test
    void addUser_withValidationNameException() {
        int userId = 1;
        User user = new User(userId, "test@email.com", null);
        UserDto userDto = UserMapper.toUserDto(user);
        when(userService.create(user)).thenReturn(user);
        mockMvc.perform(post("/users")
                        .contentType("application/json")
                        .content(mapper.writeValueAsString(userDto)))
                .andExpect(status().isBadRequest());
    }

    @SneakyThrows
    @Test
    void deleteUser() {
        int userId = 1;

        mockMvc.perform(delete("/users/{id}", userId))
                .andExpect(status().isOk());

        verify(userService).delete(userId);
    }

    @SneakyThrows
    @Test
    void updateUser() {
        int userId = 1;
        User user = new User(userId, "test@email.com", "lasa");
        UserDto userDto = UserMapper.toUserDto(user);
        when(userService.update(UserMapper.toUserDto(user), userId)).thenReturn(UserMapper.toUserDto(user));
        mockMvc.perform(patch("/users/{id}", userId)
                        .contentType("application/json")
                        .content(mapper.writeValueAsString(userDto)))
                .andExpect(status().isOk());

        verify(userService).update(UserMapper.toUserDto(user), userId);
    }

    @SneakyThrows
    @Test
    void updateUser_NotFound() {
        int userId = 1;
        User user = new User(null, null, null);
        UserDto userDto = UserMapper.toUserDto(user);
        when(userService.update(UserMapper.toUserDto(user), userId)).thenReturn(UserMapper.toUserDto(user));
        mockMvc.perform(patch("/users/{id}", userId)
                        .contentType("application/json")
                        .content(mapper.writeValueAsString(userDto)))
                .andExpect(status().isOk());

        verify(userService).update(UserMapper.toUserDto(user), userId);
    }

    @SneakyThrows
    @Test
    void updateUser_withThrow() {
        int userId = 1;
        User user = new User(userId, "test@email.com", "lasa");
        UserDto userDto = UserMapper.toUserDto(user);
        when(userService.update(UserMapper.toUserDto(user), userId)).thenThrow(EntityNotFoundException.class);
        mockMvc.perform(patch("/users/{id}", userId)
                        .contentType("application/json")
                        .content(mapper.writeValueAsString(userDto)))
                .andExpect(status().isNotFound());
    }
}