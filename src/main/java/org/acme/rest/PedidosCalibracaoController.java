package org.acme.rest;

import org.acme.dtos.*;
import org.acme.usecase.PedidosCalibracaoUseCase;

import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Path("maximosms/pedidos/calibracao")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class PedidosCalibracaoController {

    private final PedidosCalibracaoUseCase useCase;


    @Inject
    public PedidosCalibracaoController(PedidosCalibracaoUseCase useCase) {this.useCase = useCase;}

    @POST
    @Transactional
    public Response fazerPedido(@QueryParam("clientId") Long clientId,
                                PedidosCalibracaoRequest request){
        PedidosCalibracaoResponse response = useCase.fazerPedido(request, clientId);
        return Response.status(Response.Status.CREATED).entity(response).build();
    }


}
