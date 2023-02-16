package org.acme.entities;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;
@Getter
@Setter
@EqualsAndHashCode
@Entity
@Table(name = "produtos")
public class Produtos {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nome")
    private String nome;

    //ES- Equipamento de segurança
    //DG- Detector de gás
    @Column(name = "tipo", length = 2, columnDefinition = "VARCHAR(2)")
    private String tipo;

    @Column(name = "estoque")
    private Integer estoque;

    @Column(name = "valor")
    private BigDecimal valor;
}
