package org.acme.rest;

import io.quarkus.hibernate.orm.panache.PanacheQuery;
import org.acme.dtos.CriarUsuarioRequest;
import org.acme.dtos.CriarUsuarioResponse;
import org.acme.entities.Usuario;
import org.acme.repository.UsuarioRepository;
import org.acme.usecase.UsuarioUseCase;
import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("maximosms/users")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class UsuarioController {

    private final UsuarioRepository repository;
    private final UsuarioUseCase useCase;

    @Inject
    public UsuarioController(UsuarioRepository repository, UsuarioUseCase useCase) {
        this.repository = repository;
        this.useCase = useCase;
    }

    @POST
    @Transactional
    public Response criarUsuario(CriarUsuarioRequest userRequest){
        CriarUsuarioResponse response = useCase.incuirUsuario(userRequest);
        return Response.status(Response.Status.CREATED).entity(response).build();
    }

    @GET
    public Response listUsuarios(){
        PanacheQuery<Usuario> query = repository.findAll();
        return Response.ok(query.list()).build();
    }


    @DELETE
    @Transactional
    @Path("{id}")
    public Response deleteUsuario(@PathParam("id") Long id){
            useCase.deletarUsuario(id);
            return Response.noContent().build();
    }

    @PUT
    @Transactional
    @Path("{id}")
    public Response updateUsuario(@PathParam("id") Long id, CriarUsuarioRequest userRequest){
            CriarUsuarioResponse response = useCase.atualizarUsuario(userRequest, id);
            return Response.status(Response.Status.OK).entity(response).build();
    }
}

