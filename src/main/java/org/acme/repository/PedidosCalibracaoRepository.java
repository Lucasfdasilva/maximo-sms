package org.acme.repository;

import io.quarkus.hibernate.orm.panache.PanacheQuery;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import org.acme.dtos.CriarUsuarioResponse;
import org.acme.dtos.EmpresaResponse;
import org.acme.dtos.PedidosCalibracaoResponse;
import org.acme.dtos.UsuarioResponse;
import org.acme.entities.PedidosCalibracao;

import javax.enterprise.context.ApplicationScoped;
import java.util.ArrayList;
import java.util.List;

@ApplicationScoped
public class PedidosCalibracaoRepository implements PanacheRepository<PedidosCalibracao> {
    public PedidosCalibracao findByCodigo(String codigoPedido){
        return find("codigoPedido", codigoPedido).firstResult();
    }
    public List<PedidosCalibracaoResponse> listarPedidos(){
        List<PedidosCalibracaoResponse> pedidosCalibracao = new ArrayList<>();
        PanacheQuery<PedidosCalibracao> query = findAll();
        int cont = (int) query.count();
        if(cont!=0) {
            for (int i = 0; i<cont; i++) {
                PedidosCalibracao pedido = findById(query.list().get(i).getId());
                PedidosCalibracaoResponse pedidoCalibracao = PedidosCalibracaoResponse.builder()
                        .id(pedido.getId())
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
                pedidosCalibracao.add(pedidoCalibracao);
            }
        }
        return pedidosCalibracao;
    }
}
