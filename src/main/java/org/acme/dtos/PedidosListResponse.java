package org.acme.dtos;

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
    private LocalDateTime dataAprovacao;
    private LocalDateTime dataRetirada;
}
