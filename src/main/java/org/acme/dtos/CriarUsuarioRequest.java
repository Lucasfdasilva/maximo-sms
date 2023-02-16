package org.acme.dtos;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
public class CriarUsuarioRequest {
    @NotBlank(message = "nome é requerido")
    private String nome;

    @NotBlank(message = "data de nascimento é requerida")
    private String dtNascimento;

    @NotBlank(message = "email é requerido")
    private String email;

    @NotBlank(message = "senha é requerida")
    private String senha;

    @NotBlank(message = "telefone é requerido")
    private String telefone;

    @NotBlank(message = "cpf é requerido")
    private String cpf;

    //    Tipo de usuário:
    //    1-Cliente
    //    2-ADM
    @NotNull(message = "tipo de usuario é requerido")
    private Integer tipoUsuario;

    //    Status de usuário:
    //    1- Ativo
    //    2- Desativado
    //    3- Bloqueado
    @NotNull(message = "status de usuario é requerido")
    private Integer statusUsuario;
}
