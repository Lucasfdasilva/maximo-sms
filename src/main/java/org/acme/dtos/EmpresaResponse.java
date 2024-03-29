package org.acme.dtos;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class EmpresaResponse {

    private String nomeFantasia;

    private String razaoSocial;

    private String email;

    private String telefone;

    private String cnpj;

    private String cep;
}
