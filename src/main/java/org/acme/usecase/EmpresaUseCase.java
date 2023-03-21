package org.acme.usecase;

import io.quarkus.hibernate.orm.panache.PanacheQuery;
import org.acme.dtos.CriarUsuarioRequest;
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
import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.util.Set;

@ApplicationScoped
public class EmpresaUseCase {

    private final EmpresaRepository empresaRepository;
    private final UsuarioRepository usuarioRepository;
    private final Validator validator;

    @Inject
    public EmpresaUseCase(EmpresaRepository empresaRepository, UsuarioRepository usuarioRepository, Validator validator) {
        this.empresaRepository = empresaRepository;
        this.usuarioRepository = usuarioRepository;
        this.validator = validator;
    }

    public EmpresaResponse adicionarEmpresa(EmpresaRequest request){
        validarEmpresaRequest(request);
        validarCnpj(request);
        EmpresaResponse response = EmpresaResponse.builder()
                .cnpj(request.getCnpj())
                .email(request.getEmail())
                .nomeFantasia(request.getNomeFantasia())
                .razaoSocial(request.getRazaoSocial())
                .telefone(request.getTelefone())
                .cep(request.getCep())
                .build();
        persistirDados(request);
        return response;
    }

    public EmpresaResponse updateEmpresa(EmpresaRequest request, Long id){
        validarEmpresaRequest(request);
        Empresa empresa = empresaRepository.findById(id);
        validarEmpresa(empresa);
        EmpresaResponse response = EmpresaResponse.builder()
                .cnpj(request.getCnpj())
                .email(request.getEmail())
                .nomeFantasia(request.getNomeFantasia())
                .razaoSocial(request.getRazaoSocial())
                .telefone(request.getTelefone())
                .cep(request.getCep())
                .build();
        atualizarDados(request, empresa);
        return response;
    }

    public void persistirDados(EmpresaRequest request){
        Empresa empresa = new Empresa();
        empresa.setNomeFantasia(request.getNomeFantasia());
        empresa.setRazaoSocial(request.getRazaoSocial());
        empresa.setCnpj(request.getCnpj());
        empresa.setEmail(request.getEmail());
        empresa.setTelefone(request.getTelefone());
        empresa.setCep(request.getCep());
        empresaRepository.persist(empresa);
    }

    public void atualizarDados(EmpresaRequest request, Empresa empresa){
        empresa.setNomeFantasia(request.getNomeFantasia());
        empresa.setRazaoSocial(request.getRazaoSocial());
        empresa.setCnpj(request.getCnpj());
        empresa.setEmail(request.getEmail());
        empresa.setTelefone(request.getTelefone());
        empresa.setCep(request.getCep());
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
            throw new CoreRuleException(MessagemResponse.error(MensagemKeyEnum.ID_003));
        }
    }

    public void validarEmpresaRequest(EmpresaRequest request) {
        if (request==null){
            throw new CoreRuleException(MessagemResponse.error(MensagemKeyEnum.ID_001));
        }
        Set<ConstraintViolation<EmpresaRequest>> violations = validator.validate(request);
        if (!violations.isEmpty()) {
            throw new CoreRuleException(MessagemResponse.error(MensagemKeyEnum.ID_001));
        }
    }
    public void validarCnpj(EmpresaRequest request){
        Empresa empresa = empresaRepository.findByCnpj(request.getCnpj());
        if (empresa!=null){
            throw new CoreRuleException(MessagemResponse.error(MensagemKeyEnum.ID_004));
        }
    }
}
