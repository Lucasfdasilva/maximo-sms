package org.acme.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum MensagemKeyEnum {
    ESTOQUE_ERROR("Estoque insuficiente, solicitação cancelada."),
    CODIGO_NAO_ENVIADO("Código do pedido não enviado"),
    CODIGO_INVALIDO("Código do pedido inválido.");
    private String mensagem;
}
