package org.acme.rest;

import io.quarkus.hibernate.orm.panache.PanacheQuery;
import org.acme.dtos.CadastrarProdutosRequest;
import org.acme.dtos.CadastrarProdutosResponse;
import org.acme.dtos.CriarUsuarioRequest;
import org.acme.dtos.ResponseError;
import org.acme.entities.Pedidos;
import org.acme.entities.Produtos;
import org.acme.entities.Usuario;
import org.acme.repository.PedidosRepository;
import org.acme.repository.ProdutosRepository;
import org.acme.usecase.ProdutosUseCase;

import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Set;

@Path("maximosms/produtos")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class ProdutosController {

    private ProdutosRepository repository;
    private PedidosRepository pedidosRepository;
    private ProdutosUseCase useCase;
    private Validator validator;

    @Inject
    public ProdutosController(ProdutosRepository repository, ProdutosUseCase useCase, Validator validator, PedidosRepository pedidosRepository) {
        this.repository = repository;
        this.useCase = useCase;
        this.validator = validator;
        this.pedidosRepository = pedidosRepository;
    }

    @POST
    @Transactional
    public Response cadastrarProdutos(CadastrarProdutosRequest request){
        Set<ConstraintViolation<CadastrarProdutosRequest>> violations = validator.validate(request);
        if (!violations.isEmpty()){
            ResponseError responseError = ResponseError.createFromValidation(violations);
            return Response.status(400).entity(responseError).build();
        }
        CadastrarProdutosResponse response = useCase.adicionarProdutos(request);
        return Response.status(Response.Status.CREATED).entity(response).build();
    }

    @PUT
    @Transactional
    @Path("{id}")
    public Response atualizarProdutos(@PathParam("id") Long id, CadastrarProdutosRequest request){
        Produtos produto = repository.findById(id);
        if (produto != null) {
            Set<ConstraintViolation<CadastrarProdutosRequest>> violations = validator.validate(request);
            if (!violations.isEmpty()) {
                ResponseError responseError = ResponseError.createFromValidation(violations);
                return Response.status(400).entity(responseError).build();
            }
            CadastrarProdutosResponse response = useCase.atualizarProdutos(request, produto);
            return Response.status(Response.Status.OK).entity(response).build();
        }
        return Response.status(Response.Status.NOT_FOUND).build();
    }

    @GET
    public Response listProdutos(){
        PanacheQuery<Produtos> query = repository.findAll();
        return Response.ok(query.list()).build();
    }
    @DELETE
    @Transactional
    @Path("{id}")
    public Response deleteUsuario(@PathParam("id") Long id){
        Produtos produto = repository.findById(id);
        if (produto != null) {
            repository.delete(produto);
            return Response.noContent().build();
        }
        return Response.status(Response.Status.NOT_FOUND).build();
    }

}
