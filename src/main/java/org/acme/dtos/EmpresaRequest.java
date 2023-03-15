package org.acme.dtos;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class EmpresaRequest {

    @NotBlank
    private String nome;

    @NotBlank
    private String email;

    @NotBlank
    private String telefone;

    @NotBlank
    private String cnpj;
}
