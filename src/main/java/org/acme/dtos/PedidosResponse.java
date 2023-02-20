package org.acme.dtos;

import com.fasterxml.jackson.annotation.JsonInclude;
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
    @JsonInclude(value = JsonInclude.Include.NON_NULL)
    private LocalDateTime dataAprovacao;
    @JsonInclude(value = JsonInclude.Include.NON_NULL)
    private LocalDateTime dataCancelamento;
    private String status;
    @JsonInclude(value = JsonInclude.Include.NON_NULL)
    private LocalDateTime dataRetirada;
    private String codigoPedido;
}
