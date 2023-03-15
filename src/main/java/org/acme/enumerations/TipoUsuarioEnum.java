package org.acme.enumerations;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum TipoUsuarioEnum {
    CLIENTE  ((short) 1,"Cliente"),
    FUNCIONARIO  ((short) 2,"Funcionario"),
    ADM  ((short) 3,"ADM");
    private final short codigo;
    private final String descricao;
}
