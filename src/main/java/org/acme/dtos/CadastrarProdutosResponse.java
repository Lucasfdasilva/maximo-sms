package org.acme.dtos;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
public class CadastrarProdutosResponse {
    private String nome;
    private String tipo;
    private Integer estoque;
    private BigDecimal valor;
}
