package org.acme.enumerations;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum StatusUsuarioEnum {
    ATIVO  ((short) 1,"Ativo"),
    DESATIVADO  ((short) 2,"Desativado"),
    BLOQUEADO  ((short) 3,"Bloqueado");
    private final short codigo;
    private final String descricao;
}
