package br.com.ota.udemy.userserviceapi.controller;

import br.com.ota.udemy.userserviceapi.model.UserEntity;
import br.com.ota.udemy.userserviceapi.model.dto.request.UserRequestDto;
import br.com.ota.udemy.userserviceapi.model.dto.response.UserResponseDto;
import br.com.ota.udemy.userserviceapi.service.UserService;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
public class UserController {

    private final Environment env;
    private final UserService userService;

    public UserController(Environment env, UserService userService) {
        this.env = env;
        this.userService = userService;
    }

    @GetMapping("/status")
    public String status() {
        String tokenExpiration = env.getProperty("token.expiration_time");
        String port = env.getProperty("local.server.port");
        return "Working on port: " + port + " and tokenExpiration: " + tokenExpiration;
    }

    @PostMapping
    public ResponseEntity<UserResponseDto> create(@RequestBody UserRequestDto userRequestDto) {
        UserResponseDto user = userService.create(userRequestDto);
        return new ResponseEntity<>(user, HttpStatus.CREATED);
    }
}
