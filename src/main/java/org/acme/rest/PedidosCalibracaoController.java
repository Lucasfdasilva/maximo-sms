package org.acme.rest;

import io.quarkus.hibernate.orm.panache.PanacheQuery;
import org.acme.dtos.*;
import org.acme.entities.PedidosCalibracao;
import org.acme.repository.PedidosCalibracaoRepository;
import org.acme.usecase.PedidosCalibracaoUseCase;

import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("maximosms/pedidos/calibracao")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class PedidosCalibracaoController {

    private final PedidosCalibracaoUseCase useCase;
    private final PedidosCalibracaoRepository repository;

    @Inject
    public PedidosCalibracaoController(PedidosCalibracaoUseCase useCase, PedidosCalibracaoRepository repository) {
        this.useCase = useCase;
        this.repository = repository;
    }

    @POST
    @Transactional
    public Response fazerPedido(@QueryParam("clientId") Long clientId,
                                PedidosCalibracaoRequest request){
        PedidosCalibracaoResponse response = useCase.fazerPedido(request, clientId);
        return Response.status(Response.Status.CREATED).entity(response).build();
    }

    @GET
    public Response listPedidos(){
        PanacheQuery<PedidosCalibracao> query = repository.findAll();
        return Response.ok(query.list()).build();
    }


}
