package org.acme.rest;

import io.quarkus.hibernate.orm.panache.PanacheQuery;
import org.acme.dtos.CadastrarProdutosRequest;
import org.acme.dtos.CadastrarProdutosResponse;
import org.acme.entities.Produtos;
import org.acme.repository.ProdutosRepository;
import org.acme.usecase.ProdutosUseCase;
import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("maximosms/produtos")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class ProdutosController {

    private final ProdutosRepository repository;
    private final ProdutosUseCase useCase;

    @Inject
    public ProdutosController(ProdutosRepository repository, ProdutosUseCase useCase) {
        this.repository = repository;
        this.useCase = useCase;
    }

    @POST
    @Transactional
    public Response cadastrarProdutos(CadastrarProdutosRequest request){
        CadastrarProdutosResponse response = useCase.adicionarProdutos(request);
        return Response.status(Response.Status.CREATED).entity(response).build();
    }

    @PUT
    @Transactional
    @Path("{id}")
    public Response atualizarProdutos(@PathParam("id") Long id, CadastrarProdutosRequest request){
        CadastrarProdutosResponse response = useCase.atualizarProdutos(request, id);
        return Response.status(Response.Status.OK).entity(response).build();
    }
    @GET
    public Response listProdutos(){
        PanacheQuery<Produtos> query = repository.findAll();
        return Response.ok(query.list()).build();
    }
    @DELETE
    @Transactional
    @Path("{id}")
    public Response deletarProdutos(@PathParam("id") Long id) {
        useCase.deletarProduto(id);
        return Response.noContent().build();
    }
}
