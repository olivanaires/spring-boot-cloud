package br.com.ota.udemy.userserviceapi.model;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.io.Serializable;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@With
public class UserEntity implements Serializable {

    private static final long serialVersionUID = -7507106767244758793L;

    @Id
    @GeneratedValue
    private Long id;
    private String name;
    private String email;
    private String cpf;

}
