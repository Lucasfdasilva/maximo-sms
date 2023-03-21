package org.acme.dtos;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Getter
@Setter
public class AtualizarStatusPedidoRequest {
    @NotNull
    private short status;
    private Integer revisao;
    private String proposta;
    private float valor;
    private LocalDate dataAprovacao;
    private LocalDate dataCancelamento;
    private String motivoCancelamento;
}
