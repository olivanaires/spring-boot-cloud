package br.com.ota.udemy.userserviceapi.model.dto.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserResponseDto {
    private String id;
    private String name;
    private String email;
}
