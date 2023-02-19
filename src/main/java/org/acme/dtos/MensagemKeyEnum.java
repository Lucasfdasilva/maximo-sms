package org.acme.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum MensagemKeyEnum {
    ESTOQUE_ERROR("Estoque insuficiente, solicitação cancelada");
    private String mensagem;
}
