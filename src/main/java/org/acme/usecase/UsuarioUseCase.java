package org.acme.usecase;

import org.acme.dtos.*;
import org.acme.entities.Empresa;
import org.acme.entities.Usuario;
import org.acme.enumerations.MensagemKeyEnum;
import org.acme.enumerations.StatusUsuarioEnum;
import org.acme.enumerations.TipoUsuarioEnum;
import org.acme.exceptions.CoreRuleException;
import org.acme.repository.EmpresaRepository;
import org.acme.repository.UsuarioRepository;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.util.Set;

@ApplicationScoped
public class UsuarioUseCase {

    private final UsuarioRepository usuarioRepository;
    private final Validator validator;
    private final EmpresaRepository empresaRepository;

    @Inject
    public UsuarioUseCase(UsuarioRepository usuarioRepository, Validator validator, EmpresaRepository empresaRepository) {
        this.usuarioRepository = usuarioRepository;
        this.validator = validator;
        this.empresaRepository = empresaRepository;
    }

    public CriarUsuarioResponse incuirUsuario(CriarUsuarioRequest request) {
        validarCriarUsuarioRequest(request);
        validarExistenciaEmail(request);
        CriarUsuarioResponse criarUsuarioResponse = CriarUsuarioResponse.builder()
                .usuario(UsuarioResponse.builder()
                        .dtNascimento(request.getUsuario().getDtNascimento())
                        .email(request.getUsuario().getEmail())
                        .nome(request.getUsuario().getNome())
                        .build())
                .empresa(EmpresaResponse.builder()
                        .cnpj(request.getEmpresa().getCnpj())
                        .email(request.getEmpresa().getEmail())
                        .nomeFantasia(request.getEmpresa().getNomeFantasia())
                        .razaoSocial(request.getEmpresa().getRazaoSocial())
                        .telefone(request.getEmpresa().getTelefone())
                        .cep(request.getEmpresa().getCep())
                        .build())
                .build();
        persistirDados(request);
        return criarUsuarioResponse;
    }
    public CriarUsuarioResponse verificarUsuario(VerificarUsuarioRequest request) {
        validarVerificarUsuarioRequest(request);
        Usuario usuario = buscarUsuario(request);
        return CriarUsuarioResponse.builder()
                .usuario(UsuarioResponse.builder()
                        .dtNascimento(usuario.getDtNascimento())
                        .email(usuario.getEmail())
                        .nome(usuario.getNome())
                        .build())
                .empresa(EmpresaResponse.builder()
                        .cnpj(usuario.getEmpresa().getCnpj())
                        .email(usuario.getEmpresa().getEmail())
                        .nomeFantasia(usuario.getEmpresa().getNomeFantasia())
                        .razaoSocial(usuario.getEmpresa().getRazaoSocial())
                        .telefone(usuario.getEmpresa().getTelefone())
                        .cep(usuario.getEmpresa().getCep())
                        .build())
                .build();
    }

    public AtualizarUsuarioResponse atualizarUsuario(AtualizarUsuarioRequest request, Long id) {
        Usuario user = usuarioRepository.findById(id);
        validarUsuario(user);
        validarRequestAtualizar(request);
        AtualizarUsuarioResponse criarUsuarioResponse = AtualizarUsuarioResponse.builder()
                .id(user.getId())
                .senha(request.getSenha())
                .empresa(empresaRepository.findById(request.getEmpresa()))
                .dtNascimento(request.getDtNascimento())
                .email(request.getEmail())
                .statusUsuario(request.getStatusUsuario())
                .tipoUsuario(request.getTipoUsuario())
                .nome(request.getNome())
                .build();
        atualizarDados(request, user);
        return criarUsuarioResponse;
    }

    public void validarCriarUsuarioRequest(CriarUsuarioRequest request) {
        if (request==null){
            throw new CoreRuleException(MessagemResponse.error(MensagemKeyEnum.REQUEST_ERRO));
        }
        Set<ConstraintViolation<CriarUsuarioRequest>> violationsRequest = validator.validate(request);
        if (!violationsRequest.isEmpty()) {
            throw new CoreRuleException(MessagemResponse.error(MensagemKeyEnum.REQUEST_ERRO));
        }
        Set<ConstraintViolation<EmpresaRequest>> violationsEmpresa = validator.validate(request.getEmpresa());
        if (!violationsEmpresa.isEmpty()) {
            throw new CoreRuleException(MessagemResponse.error(MensagemKeyEnum.REQUEST_ERRO));
        }
        Set<ConstraintViolation<UsuarioRequest>> violationsUsuario = validator.validate(request.getUsuario());
        if (!violationsUsuario.isEmpty()) {
            throw new CoreRuleException(MessagemResponse.error(MensagemKeyEnum.REQUEST_ERRO));
        }
    }

    public void validarRequestAtualizar(AtualizarUsuarioRequest request) {
        if (request==null){
            throw new CoreRuleException(MessagemResponse.error(MensagemKeyEnum.REQUEST_ERRO));
        }
        Set<ConstraintViolation<AtualizarUsuarioRequest>> violations = validator.validate(request);
        if (!violations.isEmpty()) {
            throw new CoreRuleException(MessagemResponse.error(MensagemKeyEnum.REQUEST_ERRO));
        }
    }

    public void validarUsuario(Usuario user) {
        if (user == null) {
            throw new CoreRuleException(MessagemResponse.error(MensagemKeyEnum.USUARIO_INVALIDO));
        }
    }
    public void validarVerificarUsuarioRequest(VerificarUsuarioRequest request) {
        if (request==null){
            throw new CoreRuleException(MessagemResponse.error(MensagemKeyEnum.REQUEST_ERRO));
        }
        Set<ConstraintViolation<VerificarUsuarioRequest>> violations = validator.validate(request);
        if (!violations.isEmpty()) {
            throw new CoreRuleException(MessagemResponse.error(MensagemKeyEnum.REQUEST_ERRO));
        }
    }

    public Usuario buscarUsuario(VerificarUsuarioRequest request) {
       Usuario usuario = usuarioRepository.findByEmail(request.getEmail());
       if (usuario==null){
           throw new CoreRuleException(MessagemResponse.error(MensagemKeyEnum.EMAIL_INESISTENTE));
       }
       if (!usuario.getSenha().equals(request.getSenha())){
           throw new CoreRuleException(MessagemResponse.error(MensagemKeyEnum.SENHA_INVALIDA));
       }
       return usuario;
    }

    public void persistirDados(CriarUsuarioRequest request) {
        Usuario user = new Usuario();
        Empresa empresa = new Empresa();

       boolean empresaExiste = (buscarEmpresa(request)!=null);

        if (!empresaExiste) {
            empresa.setCnpj(request.getEmpresa().getCnpj());
            empresa.setEmail(request.getEmpresa().getEmail());
            empresa.setTelefone(request.getEmpresa().getTelefone());
            empresa.setRazaoSocial(request.getEmpresa().getRazaoSocial());
            empresa.setNomeFantasia(request.getEmpresa().getNomeFantasia());
            empresa.setCep(request.getEmpresa().getCep());
            empresaRepository.persist(empresa);
         } else {
            empresa = buscarEmpresa(request);
        }
        user.setDtNascimento(request.getUsuario().getDtNascimento());
        user.setNome(request.getUsuario().getNome());
        user.setTipoUsuario(TipoUsuarioEnum.CLIENTE.getCodigo());
        user.setEmail(request.getUsuario().getEmail());
        user.setSenha(request.getUsuario().getSenha());
        user.setStatusUsuario(StatusUsuarioEnum.ATIVO.getCodigo());
        user.setEmpresa(empresa);
        usuarioRepository.persist(user);
    }

    public void atualizarDados(AtualizarUsuarioRequest request, Usuario user) {
        user.setDtNascimento(request.getDtNascimento());
        user.setNome(request.getNome());
        user.setTipoUsuario(request.getTipoUsuario());
        user.setEmail(request.getEmail());
        user.setSenha(request.getSenha());
        user.setStatusUsuario(request.getStatusUsuario());
        user.setEmpresa(empresaRepository.findById(request.getEmpresa()));
    }

    public void deletarUsuario(Long id) {
        Usuario user = usuarioRepository.findById(id);
        validarUsuario(user);
        usuarioRepository.delete(user);
    }

    public void validarExistenciaEmail(CriarUsuarioRequest request) {
        Usuario usuario = usuarioRepository.findByEmail(request.getUsuario().getEmail());
        if (usuario!=null) {
            throw new CoreRuleException(MessagemResponse.error(MensagemKeyEnum.EMAIL_ERRO));
        }
    }
    public Empresa buscarEmpresa(CriarUsuarioRequest request){
      Empresa empresa = empresaRepository.findByCnpj(request.getEmpresa().getCnpj());
      return empresa;
    }
}
