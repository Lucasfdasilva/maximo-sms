package org.acme.dtos;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class CriarUsuarioResponse {
    private UsuarioResponse usuario;
    private EmpresaResponse empresa;

}
