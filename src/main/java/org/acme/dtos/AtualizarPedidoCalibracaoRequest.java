package org.acme.dtos;

import lombok.Getter;
import lombok.Setter;
import org.acme.entities.Usuario;

import javax.validation.constraints.NotNull;


@Getter
@Setter
public class AtualizarPedidoCalibracaoRequest {

    private Usuario cliente;

    private Integer quantidade;

    private String dataCalibracao;

    @NotNull
    private short status;

    private String revisao;

    private boolean rbc;

    private boolean taxaUrgencia;

    private String gas;

    private String numeroSerie;

    private String detectorMarca;

    private String detectorModelo;

    private float valor;
}
