package org.acme.exceptions;

import lombok.Getter;
import lombok.Setter;
import org.acme.dtos.MessagemResponse;

@Getter
@Setter
public class CoreRuleException extends ValidationException{

    public CoreRuleException(MessagemResponse mensagemErro) {super(mensagemErro);}
}
