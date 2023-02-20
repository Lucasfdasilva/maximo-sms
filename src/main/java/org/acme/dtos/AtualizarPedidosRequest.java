package org.acme.dtos;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Getter
@Setter
public class AtualizarPedidosRequest{

    private LocalDateTime dataRetirada;
    private boolean pedidoConfirmado;
}
