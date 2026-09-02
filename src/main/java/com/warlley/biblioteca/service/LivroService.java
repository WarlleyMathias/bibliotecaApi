package com.warlley.biblioteca.service;

import com.warlley.biblioteca.model.Livro;
import com.warlley.biblioteca.repository.LivroRepository;
import org.springframework.stereotype.Service;


@Service
public class LivroService {
    private final LivroRepository livroRepository;

    public LivroService(LivroRepository livroRepository) {
        this.livroRepository = livroRepository;
    }

    public Livro buscarLivro(Long aLong){
        return livroRepository.findById(aLong).orElse(new Livro());
    }

    public void deletarLivro(Long aLong){
        Livro livro = buscarLivro(aLong);
        livroRepository.deleteById(livro.getId());
    }
}
