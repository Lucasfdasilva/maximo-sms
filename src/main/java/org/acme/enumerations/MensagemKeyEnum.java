package org.acme.enumerations;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum MensagemKeyEnum {
    ID_001("Request inválido, verificar campos obrigatórios."),
    ID_002("Usuário inválido."),
    ID_003("Empresa inválida."),
    ID_004("CNPJ da empresa já cadastrado."),
    ID_005("Produto não encontrado."),
    ID_006("Estoque insuficiente, solicitação cancelada."),
    ID_007("Código do pedido não enviado"),
    ID_008("ClienteId ou ProdutoId não recebidos"),
    ID_009("Quantidade solicitada referente ao produto é inválida, é necessário inserir uma quantidade"),
    ID_010("Caso o pedido seja confirmado, é necessário haver data de retirada, caso cancelado não deve haver data de retirada"),
    ID_011("ClienteId ou ProdutoId são invalidos"),
    ID_012("Email inserido já cadastrado no nosso sistema, por favor, faça o login"),
    ID_013("Email não encontrado, por favor, faça o cadastro"),
    ID_014("Senha invalida"),
    ID_015("Código de Status inválido"),
    ID_016("É necessário enviar o numero da proposta e o valor"),
    ID_017("É necessário enviar o motivo de cancelamento"),
    ID_018("Código do pedido inválido."),
    ID_019("É necessário enviar o status");
    private String mensagem;
}
