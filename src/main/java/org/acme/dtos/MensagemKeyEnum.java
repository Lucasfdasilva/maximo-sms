package org.acme.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum MensagemKeyEnum {
    REQUEST_ERRO("Request inválido, verificar campos obrigatórios."),
    USUARIO_INVALIDO("Usuário inválido."),
    ESTOQUE_ERROR("Estoque insuficiente, solicitação cancelada."),
    CODIGO_NAO_ENVIADO("Código do pedido não enviado"),
    CLIENTE_PRODUTO_NAO_ENVIADO("ClienteId ou ProdutoId não recebidos"),
    QUANDITADE_INVALIDA("Quantidade solicitado referente ao produto é inválida, é necessário inserir uma quantidade"),
    REQUEST_UPDATE_PEDIDO("Caso o pedido seja confirmado, é necessário haver data de retirada, caso cancelado não deve haver data de retirada"),
    CLIENTE_PRODUTO_INVALIDOS("ClienteId ou ProdutoId são invalidos"),
    CODIGO_INVALIDO("Código do pedido inválido.");
    private String mensagem;
}
