package org.acme.usecase;

import java.util.Objects;
import org.acme.dtos.*;
import org.acme.entities.*;
import org.acme.enumerations.MensagemKeyEnum;
import org.acme.enumerations.StatusPedidosCalibracaoEnum;
import org.acme.exceptions.CoreRuleException;
import org.acme.repository.EmpresaRepository;
import org.acme.repository.PedidosCalibracaoRepository;
import org.acme.repository.UsuarioRepository;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.security.SecureRandom;
import java.time.LocalDate;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@ApplicationScoped
public class PedidosCalibracaoUseCase {

    private final PedidosCalibracaoRepository repository;
    private final UsuarioRepository usuarioRepository;
    private final EmpresaRepository empresaRepository;
    private final Validator validator;
    private static final String DATA_CALIBRACAO_COM_URGENCIA = "Em até 24Hrs";
    private static final String DATA_CALIBRACAO_SEM_URGENCIA = "Em até 5 dias";

    @Inject
    public PedidosCalibracaoUseCase(PedidosCalibracaoRepository repository, UsuarioRepository usuarioRepository, EmpresaRepository empresaRepository, Validator validator) {
        this.repository = repository;
        this.usuarioRepository = usuarioRepository;
        this.empresaRepository = empresaRepository;
        this.validator = validator;
    }

    public PedidosCalibracaoResponse fazerPedido(PedidosCalibracaoRequest request, Long clientId){
        Usuario usuario = usuarioRepository.findById(clientId);
        validarFazerPedido(request, usuario);
        Empresa empresa = empresaRepository.findById(usuario.getEmpresa().getId());
        PedidosCalibracaoResponse pedidosCalibracaoResponse = PedidosCalibracaoResponse.builder()
                .codigoPedido(gerarCodigo(empresa))
                .dataCalibracao(inserirDataCalibracao(request))
                .gas(request.getGas())
                .dataPedido(LocalDate.now())
                .rbc(request.isRbc())
                .taxaUrgencia(request.isTaxaUrgencia())
                .quantidade(request.getQuantidade())
                .cliente(CriarUsuarioResponse.builder()
                        .empresa(EmpresaResponse.builder()
                                .telefone(empresa.getTelefone())
                                .nomeFantasia(empresa.getNomeFantasia())
                                .razaoSocial(empresa.getRazaoSocial())
                                .email(empresa.getEmail())
                                .cnpj(empresa.getCnpj())
                                .cep(empresa.getCep())
                                .build())
                        .usuario(UsuarioResponse.builder()
                                .dtNascimento(usuario.getDtNascimento())
                                .email(usuario.getEmail())
                                .nome(usuario.getNome())
                                .build())
                        .build())
                .status(StatusPedidosCalibracaoEnum.PENDENTE.getMessage())
                .detectorMarca(request.getDetectorMarca())
                .detectorModelo(request.getDetectorModelo())
                .numeroSerie(request.getNumeroSerie())
                .build();
        persistirDados(request, usuario, pedidosCalibracaoResponse);
        return pedidosCalibracaoResponse;
    }

    public void persistirDados(PedidosCalibracaoRequest request, Usuario cliente, PedidosCalibracaoResponse response){
        PedidosCalibracao pedidosCalibracao = new PedidosCalibracao();
        pedidosCalibracao.setCodigoPedido(response.getCodigoPedido());
        pedidosCalibracao.setQuantidade(request.getQuantidade());
        pedidosCalibracao.setDataPedido(LocalDate.now());
        pedidosCalibracao.setDataCalibracao(inserirDataCalibracao(request));
        pedidosCalibracao.setCliente(cliente);
        pedidosCalibracao.setDetectorMarca(request.getDetectorMarca());
        pedidosCalibracao.setDetectorModelo(request.getDetectorModelo());
        pedidosCalibracao.setGas(request.getGas());
        pedidosCalibracao.setNumeroSerie(request.getNumeroSerie());
        pedidosCalibracao.setRbc(request.isRbc());
        pedidosCalibracao.setTaxaUrgencia(request.isTaxaUrgencia());
        pedidosCalibracao.setStatus(StatusPedidosCalibracaoEnum.PENDENTE.getMessage());
        repository.persist(pedidosCalibracao);
    }

    public String gerarCodigo(Empresa empresa) {
        //Gerar código aleatório com 5 chars
        int len = 5;
        final String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        SecureRandom random = new SecureRandom();
        String digitosIniciais = IntStream.range(0, len)
                .map(i -> random.nextInt(chars.length()))
                .mapToObj(randomIndex -> String.valueOf(chars.charAt(randomIndex)))
                .collect(Collectors.joining());
        //Concatenar com os ultimos dois digitos do CNPJ
        String digitosFinais = empresa.getCnpj().substring(empresa.getCnpj().length()-2);
        String barra = "-";
        return digitosIniciais.concat(barra).concat(digitosFinais);
    }
    public String inserirDataCalibracao(PedidosCalibracaoRequest request){
        if (request.isTaxaUrgencia()){
            return DATA_CALIBRACAO_COM_URGENCIA;
        }
        return DATA_CALIBRACAO_SEM_URGENCIA;
    }

    public PedidosCalibracaoResponse atualizarPedido(String codigoPedido, AtualizarPedidoCalibracaoRequest request){
        validarAtualizarPedidoRequest(request);
        validarCodigoPedido(codigoPedido);
        PedidosCalibracao pedido = repository.findByCodigo(codigoPedido);
        atualizarDados(request, pedido);
        return PedidosCalibracaoResponse.builder()
                .numeroSerie(pedido.getNumeroSerie())
                .dataPedido(pedido.getDataPedido())
                .rbc(pedido.isRbc())
                .gas(pedido.getGas())
                .detectorModelo(pedido.getDetectorModelo())
                .codigoPedido(pedido.getCodigoPedido())
                .detectorMarca(pedido.getDetectorMarca())
                .dataCalibracao(pedido.getDataCalibracao())
                .status(pedido.getStatus())
                .quantidade(pedido.getQuantidade())
                .taxaUrgencia(pedido.isTaxaUrgencia())
                .valor(pedido.getValor())
                .revisao(pedido.getRevisao())
                .dataAprovacao(pedido.getDataAprovacao())
                .cliente(CriarUsuarioResponse.builder()
                        .empresa(EmpresaResponse.builder()
                                .telefone(pedido.getCliente().getEmpresa().getTelefone())
                                .nomeFantasia(pedido.getCliente().getEmpresa().getNomeFantasia())
                                .razaoSocial(pedido.getCliente().getEmpresa().getRazaoSocial())
                                .email(pedido.getCliente().getEmpresa().getEmail())
                                .cnpj(pedido.getCliente().getEmpresa().getCnpj())
                                .cep(pedido.getCliente().getEmpresa().getCep())
                                .build())
                        .usuario(UsuarioResponse.builder()
                                .dtNascimento(pedido.getCliente().getDtNascimento())
                                .email(pedido.getCliente().getEmail())
                                .nome(pedido.getCliente().getNome())
                                .build())
                        .build())
                .build();
    }
    public void atualizarDados(AtualizarPedidoCalibracaoRequest request, PedidosCalibracao pedido){
        if (!Objects.isNull(request.getDataCalibracao())){
            pedido.setDataCalibracao(request.getDataCalibracao());}
        if (!Objects.isNull(request.getCliente())){
            pedido.setCliente(request.getCliente());}
        if (!Objects.isNull(request.getDetectorMarca())){
            pedido.setDetectorMarca(request.getDetectorMarca());}
        if (!Objects.isNull(request.getDetectorModelo())){
            pedido.setDetectorModelo(request.getDetectorModelo());}
        if (!Objects.isNull(request.getGas())){
            pedido.setGas(request.getGas());}
        if (!Objects.isNull(request.getNumeroSerie())){
            pedido.setNumeroSerie(request.getNumeroSerie());}
        if (request.isRbc()){
            pedido.setRbc(request.isRbc());}
        if (request.isTaxaUrgencia()){
            pedido.setTaxaUrgencia(request.isTaxaUrgencia());}
        pedido.setStatus(buscarMensagemStatus(request.getStatus()));
    }
    public void validarCodigoPedido(String codigoPedido){
        if (codigoPedido==null) {throw new CoreRuleException(MessagemResponse.error(MensagemKeyEnum.CODIGO_NAO_ENVIADO));}

        PedidosCalibracao pedido = repository.findByCodigo(codigoPedido);

        if (pedido == null) {throw new CoreRuleException(MessagemResponse.error(MensagemKeyEnum.CODIGO_INVALIDO));}
    }
    public void validarAtualizarPedidoRequest(AtualizarPedidoCalibracaoRequest request) {
        if (request==null){
            throw new CoreRuleException(MessagemResponse.error(MensagemKeyEnum.REQUEST_ERRO));
        }
        Set<ConstraintViolation<AtualizarPedidoCalibracaoRequest>> violations = validator.validate(request);
        if (!violations.isEmpty()) {
            throw new CoreRuleException(MessagemResponse.error(MensagemKeyEnum.REQUEST_ERRO));
        }
    }
    public String buscarMensagemStatus(short codigoStatus){
        if (codigoStatus==(short) 1){
            return StatusPedidosCalibracaoEnum.PENDENTE.getMessage();
        }
        if (codigoStatus==(short) 2){
            return StatusPedidosCalibracaoEnum.APROVADA.getMessage();
        }
        if (codigoStatus==(short) 3){
            return StatusPedidosCalibracaoEnum.ELABORADA.getMessage();
        }
        if (codigoStatus==(short) 4){
            return StatusPedidosCalibracaoEnum.CANCELADO.getMessage();
        }
        if (codigoStatus==(short) 5){
            return StatusPedidosCalibracaoEnum.FORA_ESCOPO.getMessage();
        }
        if (codigoStatus==(short) 6){
            return StatusPedidosCalibracaoEnum.FINALIZADO.getMessage();
        }
        else {
            throw new CoreRuleException(MessagemResponse.error(MensagemKeyEnum.STATUS_INVALIDO));
        }
    }

    public void validarFazerPedido(PedidosCalibracaoRequest request, Usuario usuario) {
        Set<ConstraintViolation<PedidosCalibracaoRequest>> violationsPedido = validator.validate(request);
        if (request==null){
            throw new CoreRuleException(MessagemResponse.error(MensagemKeyEnum.REQUEST_ERRO));}

        if (!violationsPedido.isEmpty()) {
            throw new CoreRuleException(MessagemResponse.error(MensagemKeyEnum.REQUEST_ERRO));}

        if (usuario==null){
            throw new CoreRuleException(MessagemResponse.error(MensagemKeyEnum.USUARIO_INVALIDO));
        }

        Empresa empresa = empresaRepository.findById(usuario.getEmpresa().getId());
        if (empresa==null){
            throw new CoreRuleException(MessagemResponse.error(MensagemKeyEnum.EMPRESA_INVALIDA));
        }
    }

}
