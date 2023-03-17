package org.acme.usecase;

import org.acme.dtos.*;
import org.acme.entities.*;
import org.acme.enumerations.StatusPedidoEnum;
import org.acme.enumerations.StatusPedidoMessageEnum;
import org.acme.repository.EmpresaRepository;
import org.acme.repository.PedidosCalibracaoRepository;
import org.acme.repository.UsuarioRepository;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.security.SecureRandom;
import java.time.LocalDate;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@ApplicationScoped
public class PedidosCalibracaoUseCase {

    private final PedidosCalibracaoRepository repository;
    private final UsuarioRepository usuarioRepository;
    private final EmpresaRepository empresaRepository;

    @Inject
    public PedidosCalibracaoUseCase(PedidosCalibracaoRepository repository, UsuarioRepository usuarioRepository, EmpresaRepository empresaRepository) {
        this.repository = repository;
        this.usuarioRepository = usuarioRepository;
        this.empresaRepository = empresaRepository;
    }

    public PedidosCalibracaoResponse fazerPedido(PedidosCalibracaoRequest request, Long clientId){
        Usuario usuario = usuarioRepository.findById(clientId);
        Empresa empresa = empresaRepository.findById(usuario.getEmpresa().getId());
        PedidosCalibracaoResponse pedidosCalibracaoResponse = PedidosCalibracaoResponse.builder()
                .codigoPedido(gerarCodigo())
                .dataCalibracao(request.getDataCalibracao())
                .gas(request.getGas())
                .dataPedido(LocalDate.now())
                .rbc(request.isRbc())
                .taxaUrgencia(request.isTaxaUrgencia())
                .quantidade(request.getQuantidade())
                .cliente(CriarUsuarioResponse.builder()
                        .empresa(EmpresaResponse.builder()
                                .telefone(empresa.getTelefone())
                                .nome(empresa.getNomeEmpresa())
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
                .status(StatusPedidoEnum.PENDENTE.getMessage())
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
        pedidosCalibracao.setDataPedido(LocalDate.now());
        pedidosCalibracao.setDataCalibracao(request.getDataCalibracao());
        pedidosCalibracao.setCliente(cliente);
        pedidosCalibracao.setDetectorMarca(request.getDetectorMarca());
        pedidosCalibracao.setDetectorModelo(request.getDetectorModelo());
        pedidosCalibracao.setGas(request.getGas());
        pedidosCalibracao.setNumeroSerie(request.getNumeroSerie());
        pedidosCalibracao.setRbc(request.isRbc());
        pedidosCalibracao.setTaxaUrgencia(request.isTaxaUrgencia());
        pedidosCalibracao.setStatus(StatusPedidoEnum.PENDENTE.getMessage());
        repository.persist(pedidosCalibracao);
    }

    public String gerarCodigo() {
        int len = 5;
        final String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        SecureRandom random = new SecureRandom();
        String codigo = IntStream.range(0, len)
                .map(i -> random.nextInt(chars.length()))
                .mapToObj(randomIndex -> String.valueOf(chars.charAt(randomIndex)))
                .collect(Collectors.joining());
        return codigo;
    }
}
