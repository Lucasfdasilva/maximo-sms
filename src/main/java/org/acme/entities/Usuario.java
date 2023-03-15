package org.acme.entities;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@EqualsAndHashCode
@Entity
@Table(name = "usuario")
public class Usuario extends PanacheEntityBase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nome")
    private String nome;

    @Column(name = "email")
    private String email;

    @Column(name = "senha")
    private String senha;

    @Column(name = "dtnascimento")
    private String dtNascimento;

    @ManyToOne
    @JoinColumn(name = "empresa_id")
    private Empresa empresa;

    @Column(name = "tipousuario")
    private short tipoUsuario;

    @Column(name = "statususuario")
    private short statusUsuario;
}
