package org.acme.dtos;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@Builder
public class PedidosVendasListResponse {
    private Long idPedido;
    private String cliente;
    private String produto;
    private String status;
    private Integer quantidade;
    private Float valorTotal;
    private LocalDate dataPedido;
    @JsonInclude(value = JsonInclude.Include.NON_NULL)
    private LocalDate dataAprovacao;
    @JsonInclude(value = JsonInclude.Include.NON_NULL)
    private LocalDate dataRetirada;
    @JsonInclude(value = JsonInclude.Include.NON_NULL)
    private LocalDate dataCancelamento;
    private String codigoPedido;
}
