package org.acme.enumerations;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum StatusPedidosCalibracaoEnum {

    PENDENTE  ("PENDENTE DE ENVIO"),
    ELABORADA ("PROPOSTA ELABORADA"),
    ALTERADA ("PROPOSTA ALTERADA"),
    FORA_ESCOPO ("PEDIDO FORA DO ESCOPO"),
    CANCELADO ("PEDIDO CANCELADO PELO LEAD");

    private final String message;
}
