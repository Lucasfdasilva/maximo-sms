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

    @Column(name = "telefone")
    private String telefone;

    @Column(name = "dtnascimento")
    private String dtNascimento;

    @Column(name = "cpf")
    private String cpf;

    //    Tipo de usuário:
//    1-Cliente
//    2-Adm
    @Column(name = "tipousuario")
    private Integer tipoUsuario;

    //    Status de usuário:
//    1- Ativo
//    2- Desativado
//    3- Bloqueado
    @Column(name = "statususuario")
    private Integer statusUsuario;
}
