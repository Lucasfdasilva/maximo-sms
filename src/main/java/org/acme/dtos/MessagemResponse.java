package org.acme.dtos;

import io.quarkus.runtime.annotations.RegisterForReflection;
import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@RegisterForReflection
@Builder
public class MessagemResponse implements Serializable {

    private ErroEnum messageType;
    private String messagem;

    public static MessagemResponse error(MensagemKeyEnum mensagemKeyEnum){
        return new MessagemResponse(ErroEnum.ERROR, mensagemKeyEnum.getMensagem());
    }

}
