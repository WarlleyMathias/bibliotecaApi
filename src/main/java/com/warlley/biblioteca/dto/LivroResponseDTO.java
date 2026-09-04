package com.warlley.biblioteca.dto;

import com.warlley.biblioteca.model.Livro;

public record LivroResponseDTO(
        Long id,
        String titulo,
        String autor,
        int ano,
        boolean disponivel
){
    public LivroResponseDTO(Livro livro){
        this(livro.getId(), livro.getTitulo(), livro.getAutor(), livro.getAno(), livro.getDisponivel());
    }
}


