package ru.practicum.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.client.BaseClient;
import ru.practicum.user.dto.UserDto;

import javax.validation.Valid;

@Service
public class UserClient extends BaseClient {

    private static final String API_PREFIX = "/users";

    @Autowired
    public UserClient(@Value("${shareit-server.url}") String serverUrl, RestTemplateBuilder builder) {
        super(
                builder
                        .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl + API_PREFIX))
                        .requestFactory(HttpComponentsClientHttpRequestFactory::new)
                        .build()
        );
    }


    public ResponseEntity<Object> getAll() {
        return get("");
    }

    //добавление пользователя;
    public ResponseEntity<Object> create(@Valid @RequestBody UserDto user) {
        return post("", user);
    }


    @PatchMapping("{id}") //обновление пользователя;
    public ResponseEntity<Object> update(@RequestBody UserDto user, @PathVariable int id) {
        return patch("/" + id, user);
    }

    @GetMapping("{id}")
    public ResponseEntity<Object> getUser(@PathVariable int id) {
        return get("/" + id);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Object> delete(@PathVariable int id) {
        return delete("/" + id);
    }
}
