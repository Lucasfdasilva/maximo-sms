package org.acme.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AtualizarUsuarioRequest {

    private String nome;

    private String email;

    private String senha;

    private String dtNascimento;

    private Long empresa;

    private short tipoUsuario;

    private short statusUsuario;
}
