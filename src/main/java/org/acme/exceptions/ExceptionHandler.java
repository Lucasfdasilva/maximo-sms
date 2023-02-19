package org.acme.exceptions;

import org.acme.dtos.MensagemKeyEnum;
import org.jboss.logging.Logger;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.ws.rs.core.Response;
import org.jboss.resteasy.reactive.server.ServerExceptionMapper;


public class ExceptionHandler {
    private static final Logger log = Logger.getLogger(ExceptionHandler.class);

    @ServerExceptionMapper
    public Response mapContraintViolation(ConstraintViolationException constraintViolation){
        log.error("Erro", constraintViolation);
        var violation = constraintViolation.getConstraintViolations().stream().findFirst().orElseThrow();
        return Response.status(Response.Status.BAD_REQUEST).entity(MensagemKeyEnum.valueOf(violation.getMessage())).build();
    }
    @ServerExceptionMapper
    public Response mapCoreRuleException(CoreRuleException exception){
        log.error("[CORE-RULE-EXCEPTION]:", exception);
        return Response.status(Response.Status.BAD_REQUEST).entity(exception.getMessagemResponse()).build();
    }
}
