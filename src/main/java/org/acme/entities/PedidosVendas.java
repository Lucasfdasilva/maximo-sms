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
@Table(name = "pedidosVendas")
public class PedidosVendas {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "clienteId")
    private Long clienteId;

    @Column(name = "produtoId")
    private Long produtoId;

    @Column(name = "quantidade")
    private Integer quantidade;

    @Column(name = "valorTotal")
    private Float valorTotal;

    @Column(name = "dataPedido")
    private LocalDate dataPedido;

    @Column(name = "dataAprovacao")
    private LocalDate dataAprovacao;

    @Column(name = "status")
    private String status;

    @Column(name = "dataRetirada")
    private LocalDate dataRetirada;

    @Column(name = "cliente")
    private String cliente;

    @Column(name = "produto")
    private String produto;

    @Column(name = "codigoPedido")
    private String codigoPedido;

    @Column(name = "dataCancelamento")
    private LocalDate dataCancelamento;
}
