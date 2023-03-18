package org.acme.dtos;


import com.fasterxml.jackson.annotation.JsonInclude;
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

    @JsonInclude(value = JsonInclude.Include.NON_NULL)
    private String tipoUsuario;

    @JsonInclude(value = JsonInclude.Include.NON_NULL)
    private String statusUsuario;

}
