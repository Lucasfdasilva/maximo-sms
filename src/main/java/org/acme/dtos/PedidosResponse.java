package org.acme.dtos;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.acme.entities.Produtos;
import org.acme.entities.Usuario;

import java.time.LocalDateTime;

@Builder
@Getter
@Setter
public class PedidosResponse {
    private String cliente;
    private String produto;
    private Integer quantidade;
    private String messagem;
    private Float valorTotal;
    private LocalDateTime dataAprovacao;
    private String status;
    private LocalDateTime dataRetirada;
}
