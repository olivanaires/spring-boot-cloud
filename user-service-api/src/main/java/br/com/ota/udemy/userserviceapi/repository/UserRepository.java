package br.com.ota.udemy.userserviceapi.repository;

import br.com.ota.udemy.userserviceapi.model.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<UserEntity, Long> {
    UserEntity findByName(String name);
    UserEntity findByEmail(String email);
    UserEntity findByCpf(String cpf);
}
