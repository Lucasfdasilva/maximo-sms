package org.acme.enumerations;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum StatusPedidoVendasMessageEnum {
    PEDIDO_PENDENTE  ("Pedido realizado com sucesso, guarde o código do pedido"),
    PEDIDO_CONFIRMADO  ("Pedido confirmado com sucesso!"),
    PEDIDO_CANCELADO  ("Pedido cancelado com sucesso!");
    private final String message;
}
