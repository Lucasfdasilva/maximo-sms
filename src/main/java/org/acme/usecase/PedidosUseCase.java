package org.acme.usecase;

import io.quarkus.hibernate.orm.panache.PanacheQuery;
import io.quarkus.panache.common.Parameters;
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
import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import javax.ws.rs.core.Response;
import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;


@ApplicationScoped
public class PedidosUseCase {
    private PedidosRepository repository;
    private UsuarioRepository usuarioRepository;
    private Validator validator;
    private ProdutosRepository produtosRepository;
    private static final String PEDIDO_PENDENTE = "Pedido realizado com sucesso, guarde o código do pedido";
    private static final String PENDENTE = "Pendente";
    private static final String PEDIDO_CONFIRMADO = "Pedido confirmado com sucesso!";
    private static final String CONFIRMADO = "Confirmado";
    private static final String PEDIDO_CANCELADO = "Pedido cancelado com sucesso!";
    private static final String CANCELADO = "Cancelado";



    @Inject
    public PedidosUseCase(PedidosRepository repository, UsuarioRepository usuarioRepository, ProdutosRepository produtosRepository, Validator validator) {
        this.repository = repository;
        this.usuarioRepository = usuarioRepository;
        this.produtosRepository = produtosRepository;
        this.validator = validator;
    }

    public PedidosResponse fazerPedido(PedidosRequest request, Long clientId, Long produtoId){
        Usuario usuario = usuarioRepository.findById(clientId);
        Produtos produtos = produtosRepository.findById(produtoId);
       if (request.getQuantidade()<=produtos.getEstoque()) {
           Float valor = request.getQuantidade() * produtos.getValor().floatValue();
           PedidosResponse pedidosResponse = PedidosResponse.builder()
                   .cliente(usuario.getNome())
                   .produto(produtos.getNome())
                   .quantidade(request.getQuantidade())
                   .messagem(PEDIDO_PENDENTE)
                   .status(PENDENTE)
                   .valorTotal(valor)
                   .codigoPedido(gerarCodigo())
                   .build();
           persistirDados(request, usuario, produtos, pedidosResponse);
           return pedidosResponse;
       }

        throw new CoreRuleException(MessagemResponse.error(MensagemKeyEnum.ESTOQUE_ERROR));
    }

    public void persistirDados(PedidosRequest request, Usuario cliente, Produtos produto, PedidosResponse response){
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
        pedidos.setCodigoPedido(response.getCodigoPedido());
        repository.persist(pedidos);
    }
    public PedidosListResponse listarPedidos(String codigoPedido){
        PanacheQuery<Pedidos> pedidosList = repository.listPedidosByCodigo(codigoPedido);
        long array = pedidosList.stream().count();
        if (array!=0){
            Usuario cliente = usuarioRepository.findById(pedidosList.list().get(0).getClienteId());
            Produtos produtos = produtosRepository.findById(pedidosList.list().get(0).getProdutoId());
            PedidosListResponse pedidosListResponse = PedidosListResponse.builder()
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
                    .build();
            return pedidosListResponse;
        }
        throw new CoreRuleException(MessagemResponse.error(MensagemKeyEnum.CODIGO_INVALIDO));
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
        throw new CoreRuleException(MessagemResponse.error(MensagemKeyEnum.ESTOQUE_ERROR));
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
}
