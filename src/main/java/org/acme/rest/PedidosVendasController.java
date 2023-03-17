package org.acme.rest;

import org.acme.dtos.*;
import org.acme.usecase.PedidosVendasUseCase;
import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Path("maximosms/pedidos/vendas")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class PedidosVendasController {


    private final PedidosVendasUseCase useCase;


    @Inject
    public PedidosVendasController(PedidosVendasUseCase useCase) {this.useCase = useCase;}

    @POST
    @Transactional
    public Response fazerPedido(@QueryParam("clientId") Long clientId,
                                  @QueryParam("produtoId") Long produtoId,
                                  PedidosVendasRequest request){
        PedidosVendasResponse response = useCase.fazerPedido(request, clientId, produtoId);
        return Response.status(Response.Status.CREATED).entity(response).build();
    }

    @GET
    public Response acompanharPedido(@QueryParam("codigoPedido") String codigoPedido){
        PedidosVendasListResponse pedidosList = useCase.listarPedido(codigoPedido);
        return Response.status(Response.Status.OK).entity(pedidosList).build();
    }
    @GET
    @Path("/listar")
    public Response listarPedidos(){
        List<PedidosVendasListResponse> pedidosList = useCase.listarTodosPedidos();
        return Response.status(Response.Status.OK).entity(pedidosList).build();
    }

    @PUT
    @Transactional
    @Path("{id}")
    public Response atualizarPedidos(@PathParam("id") Long id, AtualizarPedidosRequest request) {
        PedidosVendasResponse response = useCase.atualizarStatusPedido(request, id);
        return Response.status(Response.Status.OK).entity(response).build();
    }
}
