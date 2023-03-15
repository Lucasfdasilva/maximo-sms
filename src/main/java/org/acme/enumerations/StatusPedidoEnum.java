package org.acme.enumerations;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum StatusPedidoEnum {
    PENDENTE  ("Pendente"),
    CONFIRMADO  ("Confirmado"),
    CANCELADO  ("Cancelado");
    private final String message;
}
