package br.com.ota.udemy.userserviceapi.service;

import br.com.ota.udemy.userserviceapi.model.UserEntity;
import br.com.ota.udemy.userserviceapi.model.dto.request.UserRequestDto;
import br.com.ota.udemy.userserviceapi.model.dto.response.UserResponseDto;
import br.com.ota.udemy.userserviceapi.repository.UserRepository;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class UserService implements UserDetailsService {

    private final ObjectMapper mapper;
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    public UserService(UserRepository userRepository, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.mapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    public UserResponseDto create(UserRequestDto userRequestDto) {
        UserEntity user = mapper.convertValue(userRequestDto, UserEntity.class);
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        userRepository.save(user);
        return mapper.convertValue(user, UserResponseDto.class);
    }

    public UserResponseDto findByName(String name) {
        UserEntity user = userRepository.findByName(name);
        return mapper.convertValue(user, UserResponseDto.class);
    }

    public UserResponseDto findByEmail(String email) {
        UserEntity user = userRepository.findByEmail(email);
        return mapper.convertValue(user, UserResponseDto.class);
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        UserEntity userEntity = userRepository.findByEmail(email);
        if (userEntity == null) {
            throw new UsernameNotFoundException(email);
        }
        return new User(userEntity.getEmail(), userEntity.getPassword(), true, true, true, true, new ArrayList<>());
    }
}
