package org.acme.dtos;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.acme.entities.Usuario;

import java.time.LocalDate;


@Getter
@Setter
@Builder
public class AtualizarPedidoCalibracaoRequest {

    private Usuario cliente;

    private Integer quantidade;

    private String dataCalibracao;

    private Integer revisao;

    private boolean rbc;

    private boolean taxaUrgencia;

    private String gas;

    private String numeroSerie;

    private String detectorMarca;

    private String detectorModelo;

    private String proposta;

    private String status;

    private LocalDate dataCancelamento;

    private String motivoCancelamento;

    private LocalDate dataAprovacao;
    private float valor;
}
