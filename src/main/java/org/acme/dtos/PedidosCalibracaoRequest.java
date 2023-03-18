package org.acme.dtos;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
public class PedidosCalibracaoRequest {

    private boolean rbc;

    @NotNull
    private boolean taxaUrgencia;

    @NotBlank
    private String gas;

    @NotNull
    private Integer quantidade;

    private String numeroSerie;

    private String detectorMarca;

    private String detectorModelo;
}
