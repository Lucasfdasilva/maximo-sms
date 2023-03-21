package org.acme.dtos;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
public class CriarUsuarioRequest {

    @NotNull
    private UsuarioRequest usuario;

    @NotNull
    private EmpresaRequest empresa;

}
