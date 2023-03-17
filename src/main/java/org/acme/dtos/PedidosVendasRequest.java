package org.acme.dtos;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
public class PedidosVendasRequest {

    @NotNull(message = "quantidade é requerida")
    private Integer quantidade;
}
