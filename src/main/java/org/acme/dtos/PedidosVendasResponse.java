package org.acme.dtos;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Builder
@Getter
@Setter
public class PedidosVendasResponse {
    private String cliente;
    private String produto;
    private Integer quantidade;
    private String messagem;
    private Float valorTotal;
    @JsonInclude(value = JsonInclude.Include.NON_NULL)
    private LocalDate dataAprovacao;
    @JsonInclude(value = JsonInclude.Include.NON_NULL)
    private LocalDate dataCancelamento;
    private String status;
    @JsonInclude(value = JsonInclude.Include.NON_NULL)
    private LocalDate dataRetirada;
    private String codigoPedido;
}
