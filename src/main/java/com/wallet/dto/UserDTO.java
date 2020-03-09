package com.wallet.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserDTO {
    private Long id;
    @Email(message = "Email Inválido")
    private String email;
    @Length(min = 3, max = 50, message = "O nome deve conter entre 3 e 50 catacteres")
    private String name;
    @NotNull
    @Length(min = 3, message = "A senha deve conter no mínimo 3 caracteres")
    private String password;
    @NotNull(message = "Informe uma role de acesso")
    @Pattern(regexp="^(ROLE_ADMIN|ROLE_USER)$", message = "Para a role de acesso somente são aceitos os valores ROLE_ADMIN ou ROLE_USER")
    private String role;
}
