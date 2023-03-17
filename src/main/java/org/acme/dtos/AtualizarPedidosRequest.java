package org.acme.dtos;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class AtualizarPedidosRequest{

    private LocalDate dataRetirada;
    private boolean pedidoConfirmado;
}
