package org.acme.enumerations;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum StatusPedidosCalibracaoEnum {

    PENDENTE  ("PROPOSTA PENDENTE",(short)1),
    APROVADA  ("PROPOSTA APROVADA",(short)2),
    ELABORADA ("PROPOSTA ENVIADA",(short)3),
    CANCELADO ("PROPOSTA CANCELADA",(short)4),
    FORA_ESCOPO ("PEDIDO FORA DO ESCOPO",(short)5),
    FINALIZADO ("PEDIDO FINALIZADO",(short)6);

    private final String message;
    private final short codigo;
}
