package org.acme.dtos;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class PedidosListResponse {
    private Long idPedido;
    private String cliente;
    private String produto;
    private String status;
    private Integer quantidade;
    private Float valorTotal;
    private LocalDateTime dataPedido;
    @JsonInclude(value = JsonInclude.Include.NON_NULL)
    private LocalDateTime dataAprovacao;
    @JsonInclude(value = JsonInclude.Include.NON_NULL)
    private LocalDateTime dataRetirada;
    @JsonInclude(value = JsonInclude.Include.NON_NULL)
    private LocalDateTime dataCancelamento;
    private String codigoPedido;
}
