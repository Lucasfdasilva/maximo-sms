package org.acme.usecase;

import org.acme.dtos.*;
import org.acme.entities.Usuario;
import org.acme.exceptions.CoreRuleException;
import org.acme.repository.UsuarioRepository;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.util.Set;

@ApplicationScoped
public class UsuarioUseCase {

    private final UsuarioRepository repository;
    private final Validator validator;

    @Inject
    public UsuarioUseCase(UsuarioRepository repository, Validator validator) {
        this.repository = repository;
        this.validator = validator;
    }

    public CriarUsuarioResponse incuirUsuario(CriarUsuarioRequest request){
        validarRequest(request);
        CriarUsuarioResponse criarUsuarioResponse = CriarUsuarioResponse.builder()
                .dtNascimento(request.getDtNascimento())
                .email(request.getEmail())
                .telefone(request.getTelefone())
                .nome(request.getNome())
                .build();
        persistirDados(request);
        return criarUsuarioResponse;
    }
    public CriarUsuarioResponse atualizarUsuario(CriarUsuarioRequest request,Long id){
        Usuario user = repository.findById(id);
        validarUsuario(user);
        validarRequest(request);
        CriarUsuarioResponse criarUsuarioResponse = CriarUsuarioResponse.builder()
                .dtNascimento(request.getDtNascimento())
                .email(request.getEmail())
                .telefone(request.getTelefone())
                .nome(request.getNome())
                .build();
        atualizarDados(request, user);
        return criarUsuarioResponse;
    }

    public void validarRequest(CriarUsuarioRequest request){
        Set<ConstraintViolation<CriarUsuarioRequest>> violations = validator.validate(request);
        if (!violations.isEmpty()){
            throw new CoreRuleException(MessagemResponse.error(MensagemKeyEnum.REQUEST_ERRO));
        }
    }
    public void validarUsuario(Usuario user){
        if (user==null) {
            throw new CoreRuleException(MessagemResponse.error(MensagemKeyEnum.USUARIO_INVALIDO));
        }
    }

    public void persistirDados(CriarUsuarioRequest request){
        Usuario user = new Usuario();
        user.setDtNascimento(request.getDtNascimento());
        user.setNome(request.getNome());
        user.setTipoUsuario(request.getTipoUsuario());
        user.setEmail(request.getEmail());
        user.setCpf(request.getCpf());
        user.setTelefone(request.getTelefone());
        user.setSenha(request.getSenha());
        user.setStatusUsuario(request.getStatusUsuario());
        repository.persist(user);
    }
    public void atualizarDados(CriarUsuarioRequest request, Usuario user){
        user.setDtNascimento(request.getDtNascimento());
        user.setNome(request.getNome());
        user.setTipoUsuario(request.getTipoUsuario());
        user.setEmail(request.getEmail());
        user.setCpf(request.getCpf());
        user.setTelefone(request.getTelefone());
        user.setSenha(request.getSenha());
        user.setStatusUsuario(request.getStatusUsuario());
    }
    public void deletarUsuario(Long id){
        Usuario user = repository.findById(id);
        validarUsuario(user);
        repository.delete(user);
    }
}
