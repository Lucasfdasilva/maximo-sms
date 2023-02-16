package org.acme.dtos;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Getter
@Setter
public class CadastrarProdutosRequest {
    @NotBlank(message = "nome é requerido")
    private String nome;

    @NotBlank(message = "tipo é requerido")
    private String tipo;

    @NotNull(message = "estoque é requerido")
    private Integer estoque;

    @NotNull(message = "valor é requerido")
    private BigDecimal valor;
}
