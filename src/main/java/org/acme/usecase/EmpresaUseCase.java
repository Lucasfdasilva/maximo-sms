package org.acme.usecase;

import io.quarkus.hibernate.orm.panache.PanacheQuery;
import org.acme.dtos.EmpresaRequest;
import org.acme.dtos.EmpresaResponse;
import org.acme.dtos.MessagemResponse;
import org.acme.entities.Empresa;
import org.acme.entities.Usuario;
import org.acme.enumerations.MensagemKeyEnum;
import org.acme.exceptions.CoreRuleException;
import org.acme.repository.EmpresaRepository;
import org.acme.repository.UsuarioRepository;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

@ApplicationScoped
public class EmpresaUseCase {

    private final EmpresaRepository empresaRepository;
    private final UsuarioRepository usuarioRepository;

    @Inject
    public EmpresaUseCase(EmpresaRepository empresaRepository, UsuarioRepository usuarioRepository) {
        this.empresaRepository = empresaRepository;
        this.usuarioRepository = usuarioRepository;
    }

    public EmpresaResponse adicionarEmpresa(EmpresaRequest request){
        EmpresaResponse response = EmpresaResponse.builder()
                .cnpj(request.getCnpj())
                .email(request.getEmail())
                .nomeFantasia(request.getNomeFantasia())
                .razaoSocial(request.getRazaoSocial())
                .telefone(request.getTelefone())
                .build();
        persistirDados(request);
        return response;
    }
    public void persistirDados(EmpresaRequest request){
        Empresa empresa = new Empresa();
        empresa.setNomeFantasia(request.getNomeFantasia());
        empresa.setRazaoSocial(request.getRazaoSocial());
        empresa.setCnpj(request.getCnpj());
        empresa.setEmail(request.getEmail());
        empresa.setTelefone(request.getTelefone());
        empresaRepository.persist(empresa);
    }
    public void deletarEmpresa(Long id) {
        Empresa empresa = empresaRepository.findById(id);
        validarEmpresa(empresa);
        disvincularEmpresa(empresa);
        empresaRepository.delete(empresa);
    }

    public void disvincularEmpresa(Empresa empresa){
        PanacheQuery<Usuario> usuarios = usuarioRepository.findAll();
        long contUsuario = usuarios.count();
        if (contUsuario!=0) {
            for (int i = 0; i < contUsuario; i++) {
                if (usuarios.list().get(i).getEmpresa().equals(empresa)){
                    usuarios.list().get(i).setEmpresa(null);
                }
            }
        }
    }

    public void validarEmpresa(Empresa empresa) {
        if (empresa == null) {
            throw new CoreRuleException(MessagemResponse.error(MensagemKeyEnum.EMPRESA_INVALIDA));
        }
    }
}
