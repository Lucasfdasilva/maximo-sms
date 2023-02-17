package org.acme.rest;

import io.quarkus.hibernate.orm.panache.PanacheQuery;
import org.acme.dtos.*;
import org.acme.entities.Pedidos;
import org.acme.entities.Produtos;
import org.acme.entities.Usuario;
import org.acme.repository.PedidosRepository;
import org.acme.repository.ProdutosRepository;
import org.acme.repository.UsuarioRepository;
import org.acme.usecase.PedidosUseCase;


import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.Set;

@Path("maximosms/pedidos")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class PedidosController {
    private PedidosRepository pedidosRepository;
    private UsuarioRepository usuarioRepository;
    private ProdutosRepository produtosRepository;
    private PedidosUseCase useCase;
    private Validator validator;

    @Inject
    public PedidosController(PedidosRepository pedidosRepository, UsuarioRepository usuarioRepository, ProdutosRepository produtosRepository, PedidosUseCase useCase, Validator validator) {
        this.pedidosRepository = pedidosRepository;
        this.usuarioRepository = usuarioRepository;
        this.produtosRepository = produtosRepository;
        this.useCase = useCase;
        this.validator = validator;
    }

    @POST
    @Transactional
    @Path("{produtoId}")
    public Response obterProdutos(@QueryParam("clientId") Long clientId,
                                  @PathParam("produtoId") Long produtoId,
                                  PedidosRequest request){
        Usuario usuario = usuarioRepository.findById(clientId);
        Produtos produtos = produtosRepository.findById(produtoId);
        Set<ConstraintViolation<PedidosRequest>> violations = validator.validate(request);
        if (!violations.isEmpty()){
            ResponseError responseError = ResponseError.createFromValidation(violations);
            return Response.status(400).entity(responseError).build();
        }
        PedidosResponse response = useCase.fazerPedido(request, usuario, produtos);
        return Response.status(Response.Status.CREATED).entity(response).build();
    }

    @GET
    public Response acompanharPedido(@QueryParam("clientId") Long clientId, @QueryParam("pedidoId") Long pedidoId){
        PedidosListResponse pedidosList = useCase.listarPedidos(clientId, pedidoId);
        return Response.status(Response.Status.OK).entity(pedidosList).build();
    }
    @GET
    @Path("/listar")
    public Response listarPedidos(){
        PanacheQuery<Pedidos> query = pedidosRepository.findAll();
        return Response.ok(query.list()).build();
    }

    @PUT
    @Transactional
    @Path("{id}")
    public Response atualizarPedidos(@PathParam("id") Long id, AtualizarPedidosRequest request){
        Pedidos pedidos = pedidosRepository.findById(id);
        if (pedidos != null) {
            Set<ConstraintViolation<AtualizarPedidosRequest>> violations = validator.validate(request);
            if (!violations.isEmpty()) {
                ResponseError responseError = ResponseError.createFromValidation(violations);
                return Response.status(400).entity(responseError).build();
            }
            PedidosResponse response = useCase.atualizarStatusPedido(request, pedidos);
            return Response.status(Response.Status.OK).entity(response).build();
        }
        return Response.status(Response.Status.NOT_FOUND).build();
    }
}
