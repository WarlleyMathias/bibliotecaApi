package com.warlley.biblioteca.excepition;

import com.warlley.biblioteca.dto.ErroRespostaDTO;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErroRespostaDTO tratarValidacao(MethodArgumentNotValidException ex){

        List<ErroRespostaDTO.CampoErro> campoErros = ex.getBindingResult().
                getFieldErrors().stream().map(erro -> new ErroRespostaDTO.
                        CampoErro(erro.getField(),erro.getDefaultMessage())).toList();

        return new ErroRespostaDTO(
                HttpStatus.BAD_REQUEST.value(),
                "Erro de validação nos campos informados!",
                LocalDateTime.now(),
                campoErros
        );
    }
    @ExceptionHandler(ResponseStatusException.class)
    public ErroRespostaDTO tratarResponseStatus(ResponseStatusException ex){
        return new ErroRespostaDTO(
                ex.getStatusCode().value(),
                ex.getMessage(),
                LocalDateTime.now(),
                null

        );
    }

}
