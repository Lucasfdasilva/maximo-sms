package org.acme.dtos;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.acme.entities.Empresa;

@Getter
@Setter
@Builder
public class AtualizarUsuarioResponse {

    private Long id;

    private String nome;

    private String email;

    private String senha;

    private String dtNascimento;

    private Empresa empresa;

    private short tipoUsuario;

    private short statusUsuario;
}
