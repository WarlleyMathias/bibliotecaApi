package com.warlley.biblioteca.controller;

import com.warlley.biblioteca.service.LivroService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LivroController {
    private final LivroService livroService;

    public LivroController(LivroService livroService) {
        this.livroService = livroService;
    }

    @GetMapping("/livros")
    public String livros(){
        if(!livroService.buscarLivro(4L).getTitulo().isEmpty()){
            return livroService.buscarLivro(4L).getTitulo();
        }
        return "não há livro com esse titulo";
    }

    @PostMapping("/livros")
    public void deletarLivro(@RequestBody Long aLong){
        livroService.deletarLivro(aLong);
    }
}
