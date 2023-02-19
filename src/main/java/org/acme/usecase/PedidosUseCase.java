package org.acme.usecase;

import io.quarkus.hibernate.orm.panache.PanacheQuery;
import org.acme.dtos.*;
import org.acme.entities.Pedidos;
import org.acme.entities.Produtos;
import org.acme.entities.Usuario;
import org.acme.exceptions.CoreRuleException;
import org.acme.repository.PedidosRepository;
import org.acme.repository.ProdutosRepository;
import org.acme.repository.UsuarioRepository;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.core.Response;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@ApplicationScoped
public class PedidosUseCase {
    private PedidosRepository repository;
    private UsuarioRepository usuarioRepository;

    private ProdutosRepository produtosRepository;
    private static final String PEDIDO_PENDENTE = "Pedido realizado com sucesso, status: Pendente de aprovação";
    private static final String PENDENTE = "P";
    private static final String PEDIDO_CONFIRMADO = "Pedido confirmado com sucesso!";
    private static final String CONFIRMADO = "C";


    @Inject
    public PedidosUseCase(PedidosRepository repository, UsuarioRepository usuarioRepository, ProdutosRepository produtosRepository) {
        this.repository = repository;
        this.usuarioRepository = usuarioRepository;
        this.produtosRepository = produtosRepository;
    }

    public PedidosResponse fazerPedido(PedidosRequest request, Usuario cliente, Produtos produto){
       if (request.getQuantidade()<=produto.getEstoque()) {
           Float valor = request.getQuantidade() * produto.getValor().floatValue();
           PedidosResponse pedidosResponse = PedidosResponse.builder()
                   .cliente(cliente.getNome())
                   .produto(produto.getNome())
                   .quantidade(request.getQuantidade())
                   .messagem(PEDIDO_PENDENTE)
                   .valorTotal(valor)
                   .build();
           persistirDados(request, cliente, produto);
           return pedidosResponse;
       }
    //   throw new RuntimeException("Estoque insuficiente");
        throw new CoreRuleException(MessagemResponse.error(MensagemKeyEnum.ESTOQUE_ERROR));
//        throw new CoreRuleException(MessagemResponse.error(MensagemKeyEnum.ESTOQUE_ERROR));
    }

    public void persistirDados(PedidosRequest request, Usuario cliente, Produtos produto){
        Pedidos pedidos = new Pedidos();
        Float valor = request.getQuantidade()* produto.getValor().floatValue();
        pedidos.setClienteId(cliente.getId());
        pedidos.setProdutoId(produto.getId());
        pedidos.setQuantidade(request.getQuantidade());
        pedidos.setDataPedido(LocalDateTime.now());
        pedidos.setValorTotal(valor);
        pedidos.setStatus(PENDENTE);
        pedidos.setCliente(cliente.getNome());
        pedidos.setProduto(produto.getNome());
        repository.persist(pedidos);
    }
    public PedidosListResponse listarPedidos(Long clientId, Long pedidoId){
        Pedidos pedido = repository.findById(pedidoId);
        if (pedido.getClienteId().equals(clientId)) {
            Usuario cliente = usuarioRepository.findById(clientId);
            Produtos produtos = produtosRepository.findById(pedido.getProdutoId());
            PedidosListResponse pedidosListResponse = PedidosListResponse.builder()
                    .idPedido(pedido.getId())
                    .dataPedido(pedido.getDataPedido())
                    .dataAprovacao(pedido.getDataAprovacao())
                    .dataRetirada(pedido.getDataRetirada())
                    .status(pedido.getStatus())
                    .valorTotal(pedido.getValorTotal())
                    .quantidade(pedido.getQuantidade())
                    .produto(produtos.getNome())
                    .cliente(cliente.getNome())
                    .build();
            return pedidosListResponse;
        }
        return null;
    }
    public PedidosResponse atualizarStatusPedido(AtualizarPedidosRequest request, Pedidos pedidos){
       Usuario cliente = usuarioRepository.findById(pedidos.getClienteId());
       Produtos produto = produtosRepository.findById(pedidos.getProdutoId());
       if (pedidos.getQuantidade()<=produto.getEstoque()) {
           PedidosResponse pedidosResponse = PedidosResponse.builder()
                   .cliente(cliente.getNome())
                   .produto(produto.getNome())
                   .quantidade(pedidos.getQuantidade())
                   .messagem(PEDIDO_CONFIRMADO)
                   .valorTotal(pedidos.getValorTotal())
                   .dataAprovacao(LocalDateTime.now())
                   .dataRetirada(request.getDataRetirada())
                   .build();
           atualizarDados(request, pedidos);
           return pedidosResponse;
       }
       throw new RuntimeException("Estoque insuficiente");
    }
    public void atualizarDados(AtualizarPedidosRequest request, Pedidos pedidos){
        Usuario cliente = usuarioRepository.findById(pedidos.getClienteId());
        Produtos produto = produtosRepository.findById(pedidos.getProdutoId());
        produto.setEstoque(produto.getEstoque()-pedidos.getQuantidade());
        Float valor = pedidos.getQuantidade()* produto.getValor().floatValue();
        pedidos.setClienteId(cliente.getId());
        pedidos.setProdutoId(produto.getId());
        pedidos.setQuantidade(pedidos.getQuantidade());
        pedidos.setDataPedido(LocalDateTime.now());
        pedidos.setValorTotal(valor);
        pedidos.setStatus(CONFIRMADO);
        pedidos.setDataRetirada(request.getDataRetirada());
        pedidos.setDataAprovacao(LocalDateTime.now());
    }
}
