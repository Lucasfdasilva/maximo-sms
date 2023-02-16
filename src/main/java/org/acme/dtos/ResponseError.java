package org.acme.dtos;

import lombok.Getter;
import lombok.Setter;

import javax.validation.ConstraintViolation;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
@Setter
public class ResponseError {
    private String message;
    private Collection<FieldError> errors;


    public ResponseError(String message, Collection<FieldError> errors) {
        this.message = message;
        this.errors = errors;
    }

    public static <T> ResponseError createFromValidation(Set<ConstraintViolation<T>> violations){
        List<FieldError> errors = violations
                .stream()
                .map(cv -> new FieldError(cv.getPropertyPath().toString(), cv.getMessage()))
                .collect(Collectors.toList());
        String message = "Validation Error";
        var responseError = new ResponseError(message, errors);
        return responseError;
    }
}

