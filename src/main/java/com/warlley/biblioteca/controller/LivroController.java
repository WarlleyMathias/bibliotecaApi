package com.warlley.biblioteca.controller;

import com.warlley.biblioteca.dto.LivroRequestDTO;
import com.warlley.biblioteca.dto.LivroResponseDTO;
import com.warlley.biblioteca.service.LivroService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Tag(name = "Livros", description = "Endpoints para gerenciar os livros.")
public class LivroController {
    private final LivroService livroService;

    public LivroController(LivroService livroService) {
        this.livroService = livroService;
    }

    @Operation(summary = "Recebe uma lista de Livros.", description = "retorna uma lista de todos os livros mesmo que vazia.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "lista retornada com sucesso.")
    })
    @GetMapping("/livros")
    public List<LivroResponseDTO> listarLivros(){
        return livroService.buscarTodosLivros();
    }

    @Operation(summary = "Busca um Livro.", description = "Recebe id como parametro para buscar um livro no banco de dados e o retorna.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Livro buscado com sucesso."),
            @ApiResponse(responseCode = "404", description = "Id informado não existe no banco de dados.")
    })
    @GetMapping("/livros/{id}")
    public LivroResponseDTO getLivro(@PathVariable Long id){
        return livroService.buscarLivro(id);
    }

    @Operation(summary = "Adiciona um novo Livro.", description = "Recebe titulo, autor e ano, como parametros para " +
            "criação e retorna o livro criado no banco de dados.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Livro criado com sucesso."),
            @ApiResponse(responseCode = "400", description = "parametros passado são inválidos."),
            @ApiResponse(responseCode = "409", description = "conflito: já existe um livro cadastrado com esse nome.")
    })
    @PostMapping("/livros")
    @ResponseStatus(HttpStatus.CREATED)
    public LivroResponseDTO addLivro(@Valid @RequestBody LivroRequestDTO livroDTO){
        return livroService.addLivro(livroDTO);
    }

    @Operation(summary = "Atualiza um livro.", description = "Recebe o id do livro como parametro, e as alterações que serão feitas.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "livro atualizado com sucesso."),
            @ApiResponse(responseCode = "400", description = "parametros para atualizar o livro invalido."),
            @ApiResponse(responseCode = "404", description = "Id passado como parametro não encontrado no banco de dados"),
            @ApiResponse(responseCode = "409", description = "conflito: Titulo já existente.")
    })
    @PutMapping("/livros/{id}")
    public LivroResponseDTO updateLivro(@Valid @RequestBody LivroRequestDTO livroDTO, @PathVariable Long id){
        return livroService.updateLivro(livroDTO, id);
    }

    @Operation(summary = "Remove um Livro.", description = "Recebe id como parametro, e remove livro do banco de dados.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Deletado com sucesso."),
            @ApiResponse(responseCode = "404", description = "Livro não existe para ser deletado."),
            @ApiResponse(responseCode = "409", description = "conflito: bloqueou porque o livro está sendo usado por outra tabela.")
    })
    @DeleteMapping("/livros/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteLivro(@PathVariable @RequestBody Long id){
            livroService.deletarLivro(id);
    }
}
