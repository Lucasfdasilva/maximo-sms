package org.acme.dtos;

import lombok.Getter;
import lombok.Setter;
import org.acme.entities.Produtos;
import org.acme.entities.Usuario;

import javax.validation.constraints.NotNull;

@Getter
@Setter
public class PedidosRequest {

    @NotNull(message = "quantidade é requerida")
    private Integer quantidade;
}
