package org.acme.rest;

import io.quarkus.hibernate.orm.panache.PanacheQuery;
import org.acme.dtos.EmpresaRequest;
import org.acme.dtos.EmpresaResponse;
import org.acme.entities.Empresa;
import org.acme.repository.EmpresaRepository;
import org.acme.usecase.EmpresaUseCase;

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
    private final EmpresaRepository repository;

    @Inject
    public EmpresaController(EmpresaUseCase useCase, EmpresaRepository repository) {
        this.useCase = useCase;
        this.repository = repository;
    }

    @POST
    @Transactional
    public Response adicionarEmpresa(EmpresaRequest request){
        EmpresaResponse response = useCase.adicionarEmpresa(request);
        return  Response.status(Response.Status.CREATED).entity(response).build();
    }
    @PUT
    @Transactional
    @Path("{id}")
    public Response alterarEmpresa(@PathParam("id") Long id, EmpresaRequest request){
        EmpresaResponse response = useCase.updateEmpresa(request, id);
        return  Response.status(Response.Status.OK).entity(response).build();
    }
    @DELETE
    @Transactional
    @Path("{id}")
    public Response deletarEmpresa(@PathParam("id") Long id){
        useCase.deletarEmpresa(id);
        return Response.noContent().build();
    }

    @GET
    public Response listEmpresas(){
        PanacheQuery<Empresa> query = repository.findAll();
        return Response.ok(query.list()).build();
    }
}
