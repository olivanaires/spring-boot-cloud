package br.com.ota.udemy.userserviceapi.controller;

import br.com.ota.udemy.userserviceapi.model.UserEntity;
import br.com.ota.udemy.userserviceapi.model.dto.request.UserRequestDto;
import br.com.ota.udemy.userserviceapi.model.dto.response.UserResponseDto;
import br.com.ota.udemy.userserviceapi.repository.UserRepository;
import br.com.ota.udemy.userserviceapi.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Controller
public class UserController {

    private final UserRepository userRepository;

    @Autowired
    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @QueryMapping
    List<UserEntity> users() {
        return this.userRepository.findAll();
    }

    @QueryMapping
    UserEntity user(@Argument String cpf) {return this.userRepository.findByCpf(cpf);}

    @MutationMapping
    UserEntity create(@Argument UserEntity data) {
        return this.userRepository.save(data);
    }

}
