package org.acme.rest;

import org.acme.dtos.EmpresaRequest;
import org.acme.dtos.EmpresaResponse;
import org.acme.usecase.EmpresaUseCase;
import org.acme.usecase.UsuarioUseCase;

import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("maximosms/empresa")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class EmpresaController {

    private final EmpresaUseCase useCase;

    @Inject
    public EmpresaController(EmpresaUseCase useCase) {
        this.useCase = useCase;
    }
    @POST
    @Transactional
    public Response adicionarEmpresa(EmpresaRequest request){
        EmpresaResponse response = useCase.adicionarEmpresa(request);
        return  Response.status(Response.Status.CREATED).entity(response).build();
    }
    @DELETE
    @Transactional
    @Path("{id}")
    public Response deletarEmpresa(@PathParam("id") Long id){
        useCase.deletarEmpresa(id);
        return Response.noContent().build();
    }
}
