package org.acme.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum MensagemKeyEnum {
    ESTOQUE_ERROR("Estoque insuficiente, solicitação cancelada."),
    CODIGO_NAO_ENVIADO("Código do pedido não enviado"),
    CLIENTE_PRODUTO_NAO_ENVIADO("ClienteId ou ProdutoId não recebidos"),
    QUANDITADE_INVALIDA("Quantidade solicitado referente ao produto é inválida, é necessário inserir uma quantidade"),
    CODIGO_INVALIDO("Código do pedido inválido.");
    private String mensagem;
}
