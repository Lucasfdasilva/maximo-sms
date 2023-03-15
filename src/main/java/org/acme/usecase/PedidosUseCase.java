package org.acme.usecase;

import io.quarkus.hibernate.orm.panache.PanacheQuery;
import org.acme.dtos.*;
import org.acme.entities.Pedidos;
import org.acme.entities.Produtos;
import org.acme.entities.Usuario;
import org.acme.enumerations.MensagemKeyEnum;
import org.acme.enumerations.StatusPedidoEnum;
import org.acme.enumerations.StatusPedidoMessageEnum;
import org.acme.exceptions.CoreRuleException;
import org.acme.repository.PedidosRepository;
import org.acme.repository.ProdutosRepository;
import org.acme.repository.UsuarioRepository;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;


@ApplicationScoped
public class PedidosUseCase {
    private final PedidosRepository repository;
    private final UsuarioRepository usuarioRepository;
    private final ProdutosRepository produtosRepository;

    @Inject
    public PedidosUseCase(PedidosRepository repository, UsuarioRepository usuarioRepository, ProdutosRepository produtosRepository) {
        this.repository = repository;
        this.usuarioRepository = usuarioRepository;
        this.produtosRepository = produtosRepository;
    }

    public PedidosResponse fazerPedido(PedidosRequest request, Long clientId, Long produtoId){
        validarClienteEProduto(clientId, produtoId);
        Usuario usuario = usuarioRepository.findById(clientId);
        Produtos produtos = produtosRepository.findById(produtoId);
        validarEstoque(request, produtos);
        Float valor = request.getQuantidade() * produtos.getValor().floatValue();
        PedidosResponse pedidosResponse = PedidosResponse.builder()
                .cliente(usuario.getNome())
                .produto(produtos.getNome())
                .quantidade(request.getQuantidade())
                .messagem(StatusPedidoMessageEnum.PEDIDO_PENDENTE.getMessage())
                .status(StatusPedidoEnum.PENDENTE.getMessage())
                .valorTotal(valor)
                .codigoPedido(gerarCodigo())
                .build();
        persistirDados(request, usuario, produtos, pedidosResponse);
        return pedidosResponse;
    }

    public void persistirDados(PedidosRequest request, Usuario cliente, Produtos produto, PedidosResponse response){
        Pedidos pedidos = new Pedidos();
        Float valor = request.getQuantidade()* produto.getValor().floatValue();
        pedidos.setClienteId(cliente.getId());
        pedidos.setProdutoId(produto.getId());
        pedidos.setQuantidade(request.getQuantidade());
        pedidos.setDataPedido(LocalDateTime.now());
        pedidos.setValorTotal(valor);
        pedidos.setStatus(StatusPedidoEnum.PENDENTE.getMessage());
        pedidos.setCliente(cliente.getNome());
        pedidos.setProduto(produto.getNome());
        pedidos.setCodigoPedido(response.getCodigoPedido());
        repository.persist(pedidos);
    }
    public PedidosListResponse listarPedido(String codigoPedido){
        validarCodigoPedido(codigoPedido);
        PanacheQuery<Pedidos> pedidosList = repository.listPedidosByCodigo(codigoPedido);
        Usuario cliente = usuarioRepository.findById(pedidosList.list().get(0).getClienteId());
        Produtos produtos = produtosRepository.findById(pedidosList.list().get(0).getProdutoId());

        return PedidosListResponse.builder()
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
    public List<PedidosListResponse> listarTodosPedidos(){
        PanacheQuery<Pedidos> pedidosList = repository.findAll();
        int query = (int) pedidosList.stream().count()-1;
             List<PedidosListResponse> list = new ArrayList<>();
             for (int i =0; i<=query;i++) {
                 Usuario cliente = usuarioRepository.findById(pedidosList.list().get(i).getClienteId());
                 Produtos produtos = produtosRepository.findById(pedidosList.list().get(i).getProdutoId());
                 PedidosListResponse pedidosListResponse = PedidosListResponse.builder()
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
                 list.add(pedidosListResponse);
             }
            return list;
    }
    public PedidosResponse atualizarStatusPedido(AtualizarPedidosRequest request, Long id){
        Pedidos pedidos = repository.findById(id);
        Usuario cliente = usuarioRepository.findById(pedidos.getClienteId());
        Produtos produto = produtosRepository.findById(pedidos.getProdutoId());
         if (pedidos.getQuantidade()<=produto.getEstoque()) {
               PedidosResponse pedidosResponse = PedidosResponse.builder()
                       .cliente(cliente.getNome())
                       .produto(produto.getNome())
                       .quantidade(pedidos.getQuantidade())
                       .valorTotal(pedidos.getValorTotal())
                       .codigoPedido(pedidos.getCodigoPedido())
                       .build();
               if (request.isPedidoConfirmado()){
                   pedidosResponse.setStatus(StatusPedidoEnum.CONFIRMADO.getMessage());
                   pedidosResponse.setMessagem(StatusPedidoMessageEnum.PEDIDO_CONFIRMADO.getMessage());
                   pedidosResponse.setDataAprovacao(LocalDateTime.now());
                   pedidosResponse.setDataRetirada(request.getDataRetirada());
               }else {
                   pedidosResponse.setStatus(StatusPedidoEnum.CANCELADO.getMessage());
                   pedidosResponse.setMessagem(StatusPedidoMessageEnum.PEDIDO_CANCELADO.getMessage());
                   pedidosResponse.setDataCancelamento(LocalDateTime.now());
               }
               atualizarDados(request, pedidos);
               return pedidosResponse;
           }
            throw new CoreRuleException(MessagemResponse.error(MensagemKeyEnum.ESTOQUE_ERROR));
        }
    public void atualizarDados(AtualizarPedidosRequest request, Pedidos pedidos){
        Produtos produto = produtosRepository.findById(pedidos.getProdutoId());
        if ((request.isPedidoConfirmado()&&request.getDataRetirada()!=null)||(!request.isPedidoConfirmado()&&request.getDataRetirada()==null)) {
            if (request.isPedidoConfirmado()) {
                pedidos.setStatus(StatusPedidoEnum.CONFIRMADO.getMessage());
                pedidos.setDataRetirada(request.getDataRetirada());
                pedidos.setDataAprovacao(LocalDateTime.now());
                pedidos.setDataCancelamento(null);
                produto.setEstoque(produto.getEstoque() - pedidos.getQuantidade());
            } else {
                pedidos.setStatus(StatusPedidoEnum.CANCELADO.getMessage());
                pedidos.setDataCancelamento(LocalDateTime.now());
                pedidos.setDataAprovacao(null);
                pedidos.setDataRetirada(null);
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

    public void validarEstoque(PedidosRequest request, Produtos produto){
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
        PanacheQuery<Pedidos> pedidosList = repository.listPedidosByCodigo(codigoPedido);
        long array = pedidosList.stream().count();
        if (array == 0) {
            throw new CoreRuleException(MessagemResponse.error(MensagemKeyEnum.CODIGO_INVALIDO));
        }
    }
}
