package org.acme.usecase;

import io.quarkus.hibernate.orm.panache.PanacheQuery;
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
        validarRequest(request);
        validarExistenciaUsuario(request);
        CriarUsuarioResponse criarUsuarioResponse = CriarUsuarioResponse.builder()
                .usuario(UsuarioResponse.builder()
                        .dtNascimento(request.getUsuario().getDtNascimento())
                        .email(request.getUsuario().getEmail())
                        .nome(request.getUsuario().getNome())
                        .build())
                .empresa(EmpresaResponse.builder()
                        .cnpj(request.getEmpresa().getCnpj())
                        .email(request.getEmpresa().getEmail())
                        .nome(request.getEmpresa().getNome())
                        .telefone(request.getEmpresa().getTelefone())
                        .build())
                .build();
        persistirDados(request);
        return criarUsuarioResponse;
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

    public void validarRequest(CriarUsuarioRequest request) {
        Set<ConstraintViolation<CriarUsuarioRequest>> violations = validator.validate(request);
        if (!violations.isEmpty()) {
            throw new CoreRuleException(MessagemResponse.error(MensagemKeyEnum.REQUEST_ERRO));
        }
    }

    public void validarRequestAtualizar(AtualizarUsuarioRequest request) {
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
    public void validarEmpresa(Empresa empresa) {
        if (empresa == null) {
            throw new CoreRuleException(MessagemResponse.error(MensagemKeyEnum.EMPRESA_INVALIDA));
        }
    }

    public void persistirDados(CriarUsuarioRequest request) {
        Usuario user = new Usuario();
        Empresa empresa = new Empresa();
       Long empresaExiste = validarExistenciaEmpresa(request);

        if (empresaExiste==0) {
            empresa.setCnpj(request.getEmpresa().getCnpj());
            empresa.setEmail(request.getEmpresa().getEmail());
            empresa.setTelefone(request.getEmpresa().getTelefone());
            empresa.setNomeEmpresa(request.getEmpresa().getNome());
            empresaRepository.persist(empresa);
         } else {
            empresa = empresaRepository.findById(empresaExiste);
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
    public void deletarEmpresa(Long id) {
        Empresa empresa = empresaRepository.findById(id);
        validarEmpresa(empresa);
        disvincularEmpresa(empresa);
        empresaRepository.delete(empresa);
    }

    public void validarExistenciaUsuario(CriarUsuarioRequest request) {
        PanacheQuery<Usuario> usuarios = usuarioRepository.findAll();
        long contUsuario = usuarios.count();
        if (contUsuario!=0) {
            for (int i = 0; i < contUsuario; i++) {
                String email = usuarios.list().get(i).getEmail();
                if (email.equals(request.getUsuario().getEmail())) {
                    throw new CoreRuleException(MessagemResponse.error(MensagemKeyEnum.EMAIL_ERRO));
                }
            }
        }
    }
    public Long validarExistenciaEmpresa(CriarUsuarioRequest request){
        PanacheQuery<Empresa> empresas = empresaRepository.findAll();
        long contEmpresa = empresas.count();
        if (contEmpresa!=0) {
            for (int i = 0; i < contEmpresa; i++) {
                String cnpj = empresas.list().get(i).getCnpj();
                if (cnpj.equals(request.getEmpresa().getCnpj())) {
                    return empresas.list().get(i).getId();
                }
            }
        }
        Long ExistenciaFalsa = 0L;
        return ExistenciaFalsa;
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
}
