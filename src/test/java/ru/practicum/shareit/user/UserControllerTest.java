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