package com.warlley.biblioteca.controller;

import com.warlley.biblioteca.model.Livro;
import com.warlley.biblioteca.service.LivroService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class LivroController {
    private final LivroService livroService;

    public LivroController(LivroService livroService) {
        this.livroService = livroService;
    }

    @GetMapping("/livrosMenu")
    public String livros(){
        return """      
                        BIBLIOTECA\s
                        MENU\s
                        DIGITE 1 PARA CADASTRAS LIVRO -- DIGITE 2 PARA LISTA LIVROS\s
                        DIGITE 3 PARA BUSCAR LIVRO -- DIGITE 4 PARA EMPRESTAR LIVRO\s
                        DIGITE 5 PARA DEVOLVER LIVRO -- DIGITE 6 PARA REMOVER LIVRO\s
                        DIGITE 7 PARA SAIR""";
    }

    @GetMapping("/livros")
    public List<Livro> listaLivros(){
        return livroService.buscarTodosLivros();
    }

    @GetMapping("/livros/{id}")
    public Livro getLivro(@PathVariable Long id){
        return livroService.buscarLivro(id);
    }

    @PostMapping("/livros")
    @ResponseStatus(HttpStatus.CREATED)
    public Livro addLivro(@RequestBody Livro livro){
        return livroService.addLivro(livro);
    }

    @PutMapping("/livros/{id}")
    public Livro updateLivro(@RequestBody Livro livro, @PathVariable Long id){
        return livroService.updateLivro(livro, id);
    }

    @DeleteMapping("/livros/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteLivro(@PathVariable @RequestBody Long id){
            livroService.deletarLivro(id);
    }
}
