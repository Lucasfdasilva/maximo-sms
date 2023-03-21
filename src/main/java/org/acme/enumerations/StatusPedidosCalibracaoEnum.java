package org.acme.enumerations;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum StatusPedidosCalibracaoEnum {

    PENDENTE  ("PROPOSTA PENDENTE",(short)1),
    ENVIADA ("PROPOSTA ENVIADA",(short)2),
    APROVADA  ("PROPOSTA APROVADA",(short)3),
    CANCELADA ("PROPOSTA CANCELADA",(short)4),
    FORA_ESCOPO ("PEDIDO FORA DO ESCOPO",(short)5),
    FINALIZADO ("PEDIDO FINALIZADO",(short)6);

    private final String message;
    private final short codigo;
}
