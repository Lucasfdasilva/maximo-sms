package org.acme.rest;

import io.quarkus.hibernate.orm.panache.PanacheQuery;
import org.acme.dtos.CriarUsuarioRequest;
import org.acme.dtos.CriarUsuarioResponse;
import org.acme.dtos.ResponseError;
import org.acme.entities.Usuario;
import org.acme.repository.UsuarioRepository;
import org.acme.usecase.UsuarioUseCase;

import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Set;

@Path("/users")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class UsuarioController {

    private UsuarioRepository repository;
    private Validator validator;
    private UsuarioUseCase useCase;

    @Inject
    public UsuarioController(UsuarioRepository repository, Validator validator, UsuarioUseCase useCase) {
        this.repository = repository;
        this.validator = validator;
        this.useCase = useCase;
    }

    @POST
    @Transactional
    public Response criarUsuario(CriarUsuarioRequest userRequest){
        Set<ConstraintViolation<CriarUsuarioRequest>> violations = validator.validate(userRequest);
        if (!violations.isEmpty()){
            ResponseError responseError = ResponseError.createFromValidation(violations);
            return Response.status(400).entity(responseError).build();
        }
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
        Usuario user = repository.findById(id);
        if (user != null) {
            repository.delete(user);
            return Response.noContent().build();
        }
        return Response.status(Response.Status.NOT_FOUND).build();


    }

    @PUT
    @Transactional
    @Path("{id}")
    public Response updateUsuario(@PathParam("id") Long id, CriarUsuarioRequest userRequest){
        Usuario user = repository.findById(id);
        if (user != null) {
            Set<ConstraintViolation<CriarUsuarioRequest>> violations = validator.validate(userRequest);
            if (!violations.isEmpty()){
                ResponseError responseError = ResponseError.createFromValidation(violations);
                return Response.status(400).entity(responseError).build();
            }
            user.setDtNascimento(userRequest.getDtNascimento());
            user.setNome(userRequest.getNome());
            user.setTipoUsuario(userRequest.getTipoUsuario());
            user.setEmail(userRequest.getEmail());
            user.setCpf(userRequest.getCpf());
            user.setTelefone(userRequest.getTelefone());
            user.setSenha(userRequest.getSenha());
            user.setStatusUsuario(userRequest.getStatusUsuario());
            return Response.ok(user).build();

        }
        return Response.status(Response.Status.NOT_FOUND).build();
    }
}

