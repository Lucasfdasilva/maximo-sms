package org.acme.enumerations;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ErroEnum {
    ERROR("Erro");
    private String description;
}
