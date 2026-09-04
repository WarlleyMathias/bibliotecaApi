package com.warlley.biblioteca.dto;

import java.time.LocalDateTime;
import java.util.List;

public record ErroRespostaDTO(
        int status,
        String mensagem,
        LocalDateTime dataHora,
        List<CampoErro> erros
) {
    public record CampoErro(String campo, String mensagem){

    }
}
