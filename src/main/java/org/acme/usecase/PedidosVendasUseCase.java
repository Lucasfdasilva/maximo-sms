package org.acme.usecase;

import io.quarkus.hibernate.orm.panache.PanacheQuery;
import org.acme.dtos.*;
import org.acme.entities.PedidosVendas;
import org.acme.entities.Produtos;
import org.acme.entities.Usuario;
import org.acme.enumerations.MensagemKeyEnum;
import org.acme.enumerations.StatusPedidoEnum;
import org.acme.enumerations.StatusPedidoMessageEnum;
import org.acme.exceptions.CoreRuleException;
import org.acme.repository.PedidosVendasRepository;
import org.acme.repository.ProdutosRepository;
import org.acme.repository.UsuarioRepository;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.security.SecureRandom;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;


@ApplicationScoped
public class PedidosVendasUseCase {
    private final PedidosVendasRepository repository;
    private final UsuarioRepository usuarioRepository;
    private final ProdutosRepository produtosRepository;

    @Inject
    public PedidosVendasUseCase(PedidosVendasRepository repository, UsuarioRepository usuarioRepository, ProdutosRepository produtosRepository) {
        this.repository = repository;
        this.usuarioRepository = usuarioRepository;
        this.produtosRepository = produtosRepository;
    }

    public PedidosVendasResponse fazerPedido(PedidosVendasRequest request, Long clientId, Long produtoId){
        validarClienteEProduto(clientId, produtoId);
        Usuario usuario = usuarioRepository.findById(clientId);
        Produtos produtos = produtosRepository.findById(produtoId);
        validarEstoque(request, produtos);
        Float valor = request.getQuantidade() * produtos.getValor().floatValue();
        PedidosVendasResponse pedidosVendasResponse = PedidosVendasResponse.builder()
                .cliente(usuario.getNome())
                .produto(produtos.getNome())
                .quantidade(request.getQuantidade())
                .messagem(StatusPedidoMessageEnum.PEDIDO_PENDENTE.getMessage())
                .status(StatusPedidoEnum.PENDENTE.getMessage())
                .valorTotal(valor)
                .codigoPedido(gerarCodigo())
                .build();
        persistirDados(request, usuario, produtos, pedidosVendasResponse);
        return pedidosVendasResponse;
    }

    public void persistirDados(PedidosVendasRequest request, Usuario cliente, Produtos produto, PedidosVendasResponse response){
        PedidosVendas pedidosVendas = new PedidosVendas();
        Float valor = request.getQuantidade()* produto.getValor().floatValue();
        pedidosVendas.setClienteId(cliente.getId());
        pedidosVendas.setProdutoId(produto.getId());
        pedidosVendas.setQuantidade(request.getQuantidade());
        pedidosVendas.setDataPedido(LocalDate.now());
        pedidosVendas.setValorTotal(valor);
        pedidosVendas.setStatus(StatusPedidoEnum.PENDENTE.getMessage());
        pedidosVendas.setCliente(cliente.getNome());
        pedidosVendas.setProduto(produto.getNome());
        pedidosVendas.setCodigoPedido(response.getCodigoPedido());
        repository.persist(pedidosVendas);
    }
    public PedidosVendasListResponse listarPedido(String codigoPedido){
        validarCodigoPedido(codigoPedido);
        PanacheQuery<PedidosVendas> pedidosList = repository.listPedidosByCodigo(codigoPedido);
        Usuario cliente = usuarioRepository.findById(pedidosList.list().get(0).getClienteId());
        Produtos produtos = produtosRepository.findById(pedidosList.list().get(0).getProdutoId());

        return PedidosVendasListResponse.builder()
                .idPedido(pedidosList.list().get(0).getId())
                .dataPedido(pedidosList.list().get(0).getDataPedido())
                .dataAprovacao(pedidosList.list().get(0).getDataAprovacao())
                .dataRetirada(pedidosList.list().get(0).getDataRetirada())
                .status(pedidosList.list().get(0).getStatus())
                .valorTotal(pedidosList.list().get(0).getValorTotal())
                .quantidade(pedidosList.list().get(0).getQuantidade())
                .produto(produtos.getNome())
                .cliente(cliente.getNome())
                .codigoPedido(pedidosList.list().get(0).getCodigoPedido())
                .dataCancelamento(pedidosList.list().get(0).getDataCancelamento())
                .build();
    }
    public List<PedidosVendasListResponse> listarTodosPedidos(){
        PanacheQuery<PedidosVendas> pedidosList = repository.findAll();
        int query = (int) pedidosList.stream().count()-1;
             List<PedidosVendasListResponse> list = new ArrayList<>();
             for (int i =0; i<=query;i++) {
                 Usuario cliente = usuarioRepository.findById(pedidosList.list().get(i).getClienteId());
                 Produtos produtos = produtosRepository.findById(pedidosList.list().get(i).getProdutoId());
                 PedidosVendasListResponse pedidosVendasListResponse = PedidosVendasListResponse.builder()
                         .idPedido(pedidosList.list().get(i).getId())
                         .dataPedido(pedidosList.list().get(i).getDataPedido())
                         .dataAprovacao(pedidosList.list().get(i).getDataAprovacao())
                         .dataRetirada(pedidosList.list().get(i).getDataRetirada())
                         .status(pedidosList.list().get(i).getStatus())
                         .valorTotal(pedidosList.list().get(i).getValorTotal())
                         .quantidade(pedidosList.list().get(i).getQuantidade())
                         .produto(produtos.getNome())
                         .cliente(cliente.getNome())
                         .codigoPedido(pedidosList.list().get(i).getCodigoPedido())
                         .build();
                 list.add(pedidosVendasListResponse);
             }
            return list;
    }
    public PedidosVendasResponse atualizarStatusPedido(AtualizarPedidosRequest request, Long id){
        PedidosVendas pedidosVendas = repository.findById(id);
        Usuario cliente = usuarioRepository.findById(pedidosVendas.getClienteId());
        Produtos produto = produtosRepository.findById(pedidosVendas.getProdutoId());
         if (pedidosVendas.getQuantidade()<=produto.getEstoque()) {
               PedidosVendasResponse pedidosVendasResponse = PedidosVendasResponse.builder()
                       .cliente(cliente.getNome())
                       .produto(produto.getNome())
                       .quantidade(pedidosVendas.getQuantidade())
                       .valorTotal(pedidosVendas.getValorTotal())
                       .codigoPedido(pedidosVendas.getCodigoPedido())
                       .build();
               if (request.isPedidoConfirmado()){
                   pedidosVendasResponse.setStatus(StatusPedidoEnum.CONFIRMADO.getMessage());
                   pedidosVendasResponse.setMessagem(StatusPedidoMessageEnum.PEDIDO_CONFIRMADO.getMessage());
                   pedidosVendasResponse.setDataAprovacao(LocalDate.now());
                   pedidosVendasResponse.setDataRetirada(request.getDataRetirada());
               }else {
                   pedidosVendasResponse.setStatus(StatusPedidoEnum.CANCELADO.getMessage());
                   pedidosVendasResponse.setMessagem(StatusPedidoMessageEnum.PEDIDO_CANCELADO.getMessage());
                   pedidosVendasResponse.setDataCancelamento(LocalDate.now());
               }
               atualizarDados(request, pedidosVendas);
               return pedidosVendasResponse;
           }
            throw new CoreRuleException(MessagemResponse.error(MensagemKeyEnum.ESTOQUE_ERROR));
        }
    public void atualizarDados(AtualizarPedidosRequest request, PedidosVendas pedidosVendas){
        Produtos produto = produtosRepository.findById(pedidosVendas.getProdutoId());
        if ((request.isPedidoConfirmado()&&request.getDataRetirada()!=null)||(!request.isPedidoConfirmado()&&request.getDataRetirada()==null)) {
            if (request.isPedidoConfirmado()) {
                pedidosVendas.setStatus(StatusPedidoEnum.CONFIRMADO.getMessage());
                pedidosVendas.setDataRetirada(request.getDataRetirada());
                pedidosVendas.setDataAprovacao(LocalDate.now());
                pedidosVendas.setDataCancelamento(null);
                produto.setEstoque(produto.getEstoque() - pedidosVendas.getQuantidade());
            } else {
                pedidosVendas.setStatus(StatusPedidoEnum.CANCELADO.getMessage());
                pedidosVendas.setDataCancelamento(LocalDate.now());
                pedidosVendas.setDataAprovacao(null);
                pedidosVendas.setDataRetirada(null);
            }
        } else {
            throw new CoreRuleException(MessagemResponse.error(MensagemKeyEnum.REQUEST_UPDATE_PEDIDO));
        }
    }
    public void validarClienteEProduto(Long clientId, Long produtoId){
        if (clientId==null||produtoId==null) {
            throw new CoreRuleException(MessagemResponse.error(MensagemKeyEnum.CLIENTE_PRODUTO_NAO_ENVIADO));
        } else {
            Usuario usuario = usuarioRepository.findById(clientId);
            Produtos produtos = produtosRepository.findById(produtoId);
            if (usuario == null || produtos == null) {
                throw new CoreRuleException(MessagemResponse.error(MensagemKeyEnum.CLIENTE_PRODUTO_INVALIDOS));
            }
        }
    }
    public String gerarCodigo(){
        int len = 5;
        final String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        SecureRandom random = new SecureRandom();
        String codigo = IntStream.range(0, len)
                .map(i -> random.nextInt(chars.length()))
                .mapToObj(randomIndex -> String.valueOf(chars.charAt(randomIndex)))
                .collect(Collectors.joining());
        return codigo;
    }

    public void validarEstoque(PedidosVendasRequest request, Produtos produto){
        if (request.getQuantidade()==null||request.getQuantidade()==0) {
            throw new CoreRuleException(MessagemResponse.error(MensagemKeyEnum.QUANDITADE_INVALIDA));
        }
        if (request.getQuantidade() > produto.getEstoque()) {
            throw new CoreRuleException(MessagemResponse.error(MensagemKeyEnum.ESTOQUE_ERROR));
        }
    }

    public void validarCodigoPedido(String codigoPedido){
        if (codigoPedido==null) {
            throw new CoreRuleException(MessagemResponse.error(MensagemKeyEnum.CODIGO_NAO_ENVIADO));
        }
        PanacheQuery<PedidosVendas> pedidosList = repository.listPedidosByCodigo(codigoPedido);
        long array = pedidosList.stream().count();
        if (array == 0) {
            throw new CoreRuleException(MessagemResponse.error(MensagemKeyEnum.CODIGO_INVALIDO));
        }
    }
}
