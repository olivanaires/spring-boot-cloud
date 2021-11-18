package br.com.ota.udemy.userserviceapi.mutation;

import br.com.ota.udemy.userserviceapi.model.UserEntity;
import br.com.ota.udemy.userserviceapi.repository.UserRepository;
import graphql.kickstart.tools.GraphQLMutationResolver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UserMutation implements GraphQLMutationResolver {

    private final UserRepository userRepository;

    @Autowired
    public UserMutation(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    UserEntity create(UserEntity user) {
        return this.userRepository.save(user);
    }
}
