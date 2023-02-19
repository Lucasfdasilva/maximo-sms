package org.acme.exceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.acme.dtos.MessagemResponse;


@Getter
@Setter
public class ValidationException extends RuntimeException{
    private MessagemResponse messagemResponse;

   public ValidationException(MessagemResponse messagemError){this.messagemResponse = messagemError;}

}
