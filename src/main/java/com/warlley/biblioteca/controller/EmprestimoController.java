package com.warlley.biblioteca.controller;

import com.warlley.biblioteca.dto.EmprestimoRequestDTO;
import com.warlley.biblioteca.dto.EmprestimoResponseDTO;
import com.warlley.biblioteca.service.EmprestimoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Tag(name = "Emprestimos", description = "Endpoints para o gerenciamento dos emprestimos.")
public class EmprestimoController {
    private final EmprestimoService emprestimoService;

    public EmprestimoController(EmprestimoService emprestimoService){
        this.emprestimoService = emprestimoService;
    }

    @Operation(summary = "Retorna uma Lista de emprestimos.", description = "Retorna todos os emprestimos do banco de dados.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "retorna um lista de emprestimo mesmo que vazia.")
    })
    @GetMapping("/emprestimos")
    public List<EmprestimoResponseDTO> getEmprestimos(){
        return emprestimoService.buscarTodosEmprestimo();
    }

    @Operation(summary = "Devolução de livro emprestado.",description = "recebe o id do emprestimo, e deleta ele do banco de dados, atualizando o livro pra disponiveel TRUE.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "devolucao efetuada com sucesso."),
            @ApiResponse(responseCode = "404", description = "emprestimo não existe, para ser deletado."),
            @ApiResponse(responseCode = "409", description = "conflito: bloqueou, porque o emprestimo está sendo usado por outra tabela.")
    })
    @DeleteMapping("/emprestimos/{id}/devolucao")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void devolucao(@PathVariable Long id){
        emprestimoService.removeEmprestimo(id);
    }

    @Operation(summary = "Adiciona um novo emprestimo.", description = "recebe o id do usuario e o id do livro, para efetuar o emprestimo, deixando o Livro indisponivel.")
    @ApiResponses({
            @ApiResponse(responseCode = "201",description = "emprestimo efetuado com sucesso."),
            @ApiResponse(responseCode = "400", description = "parametros inválidos."),
            @ApiResponse(responseCode = "409", description = "conflito: livro já está emprestado.")
    })
    @PostMapping("/emprestimos/add")
    @ResponseStatus(HttpStatus.CREATED)
    public EmprestimoResponseDTO addEmprestimo(@Valid @RequestBody EmprestimoRequestDTO emprestimoDTO){
        return emprestimoService.addEmprestimo(emprestimoDTO);
    }
}
