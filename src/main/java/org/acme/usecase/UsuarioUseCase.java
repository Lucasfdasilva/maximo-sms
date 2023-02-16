package org.acme.usecase;

import org.acme.dtos.CriarUsuarioRequest;
import org.acme.dtos.CriarUsuarioResponse;
import org.acme.dtos.ResponseError;
import org.acme.entities.Usuario;
import org.acme.repository.UsuarioRepository;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import javax.ws.rs.core.Response;
import java.util.Set;

@ApplicationScoped
public class UsuarioUseCase {

    private UsuarioRepository repository;
    private Validator validator;

    @Inject
    public UsuarioUseCase(UsuarioRepository repository, Validator validator) {
        this.repository = repository;
        this.validator = validator;
    }

    public CriarUsuarioResponse incuirUsuario(CriarUsuarioRequest request){
        CriarUsuarioResponse criarUsuarioResponse = CriarUsuarioResponse.builder()
                .dtNascimento(request.getDtNascimento())
                .email(request.getEmail())
                .telefone(request.getTelefone())
                .nome(request.getNome())
                .build();
        persistirDados(request);
        return criarUsuarioResponse;
    }
    public CriarUsuarioResponse atualizarUsuario(CriarUsuarioRequest request, Usuario user){
        CriarUsuarioResponse criarUsuarioResponse = CriarUsuarioResponse.builder()
                .dtNascimento(request.getDtNascimento())
                .email(request.getEmail())
                .telefone(request.getTelefone())
                .nome(request.getNome())
                .build();
        atualizarDados(request, user);
        return criarUsuarioResponse;
    }

    public void validarUsuario(CriarUsuarioRequest request){
        Set<ConstraintViolation<CriarUsuarioRequest>> violations = validator.validate(request);
        if (!violations.isEmpty()){
            ResponseError responseError = ResponseError.createFromValidation(violations);
           throw new RuntimeException(responseError.toString());
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
}
