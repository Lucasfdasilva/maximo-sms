package org.acme.dtos;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class UsuarioRequest {

    @NotBlank
    private String nome;

    @NotBlank
    private String dtNascimento;

    @NotBlank
    private String email;

    @NotBlank
    private String senha;

}
