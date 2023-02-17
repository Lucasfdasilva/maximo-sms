package org.acme.entities;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@EqualsAndHashCode
@Entity
public class Pedidos {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "clienteId")
    private Long cliente;

    @Column(name = "produtoId")
    private Long produto;

    @Column(name = "quantidade")
    private Integer quantidade;

    @Column(name = "valorTotal")
    private Float valorTotal;

    @Column(name = "dataPedido")
    private LocalDateTime dataPedido;

    @Column(name = "dataAprovacao")
    private LocalDateTime dataAprovacao;

    @Column(name = "status", length = 1, columnDefinition = "VARCHAR(1)")
    private String status;

    @Column(name = "dataRetirada")
    private LocalDateTime dataRetirada;
}
