package org.acme.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CriarUsuarioRequest {

    private UsuarioRequest usuario;

    private EmpresaRequest empresa;

}
