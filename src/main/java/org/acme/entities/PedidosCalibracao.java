package org.acme.entities;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;

@Getter
@Setter
@EqualsAndHashCode
@Entity
@Table(name = "pedidosCalibracao")
public class PedidosCalibracao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "cliente_id")
    private Usuario cliente;

    @Column(name = "motivoCancelamento")
    private String motivoCancelamento;

    @Column(name = "quantidade")
    private Integer quantidade;

    @Column(name = "dataCalibracao")
    private String dataCalibracao;

    @Column(name = "dataPedido")
    private LocalDate dataPedido;

    @Column(name = "dataAprovacao")
    private LocalDate dataAprovacao;

    @Column(name = "status")
    private String status;

    @Column(name = "revisao")
    private Integer revisao;

    @Column(name = "rbc")
    private boolean rbc;

    @Column(name = "taxaUrgencia")
    private boolean taxaUrgencia;

    @Column(name = "gas")
    private String gas;

    @Column(name = "numeroSerie")
    private String numeroSerie;

    @Column(name = "detectorMarca")
    private String detectorMarca;

    @Column(name = "detectorModelo")
    private String detectorModelo;

    @Column(name = "codigoPedido")
    private String codigoPedido;

    @Column(name = "proposta")
    private String proposta;

    @Column(name = "dataCancelamento")
    private LocalDate dataCancelamento;

    @Column(name = "valor")
    private float valor;
}
