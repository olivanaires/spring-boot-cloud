package br.com.ota.udemy.userserviceapi.resolver;

import br.com.ota.udemy.userserviceapi.model.UserEntity;
import br.com.ota.udemy.userserviceapi.repository.UserRepository;
import graphql.kickstart.tools.GraphQLQueryResolver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class UserResolver implements GraphQLQueryResolver {

    private final UserRepository userRepository;

    @Autowired
    public UserResolver(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    List<UserEntity> users() {
        return this.userRepository.findAll();
    }

    void create(UserEntity user) {
        this.userRepository.save(user);
    }
}
