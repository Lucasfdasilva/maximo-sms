package org.acme.dtos;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Getter
@Setter
public class PedidosCalibracaoRequest {

    @NotNull(message = "quantidade é requerida")
    private LocalDate dataCalibracao;

    private boolean rbc;

    private boolean taxaUrgencia;

    private String gas;

    private Integer quantidade;

    private String numeroSerie;

    private String detectorMarca;

    private String detectorModelo;
}
