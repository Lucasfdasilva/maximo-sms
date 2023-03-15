package org.acme.entities;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@EqualsAndHashCode
@Entity
public class Empresa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nome_empresa")
    private String nomeEmpresa;

    @Column(name = "cnpj")
    private String cnpj;

    @Column(name = "telefone")
    private String telefone;

    @Column(name = "email")
    private String email;
}
