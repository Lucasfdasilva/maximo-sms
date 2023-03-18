package org.acme.dtos;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
@Builder
@Getter
@Setter
public class PedidosCalibracaoResponse {

    private CriarUsuarioResponse cliente;
    @JsonInclude(value = JsonInclude.Include.NON_NULL)
    private LocalDate dataAprovacao;
    @JsonInclude(value = JsonInclude.Include.NON_NULL)
    private String dataCalibracao;
    private LocalDate dataPedido;
    @JsonInclude(value = JsonInclude.Include.NON_NULL)
    private LocalDate dataCancelamento;
    @JsonInclude(value = JsonInclude.Include.NON_NULL)
    private String motivoCancelamento;
    @JsonInclude(value = JsonInclude.Include.NON_NULL)
    private Float valor;
    private boolean rbc;
    private boolean taxaUrgencia;
    private String gas;
    private Integer quantidade;
    private String numeroSerie;
    private String detectorMarca;
    private String detectorModelo;
    private String status;
    private Integer revisao;
    private String codigoPedido;
}
