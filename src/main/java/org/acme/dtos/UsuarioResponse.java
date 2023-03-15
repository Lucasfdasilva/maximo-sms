package org.acme.dtos;


import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class UsuarioResponse {

    private String nome;

    private String dtNascimento;

    private String email;

}
