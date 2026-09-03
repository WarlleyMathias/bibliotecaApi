package com.warlley.biblioteca.service;

import com.warlley.biblioteca.model.Livro;
import com.warlley.biblioteca.repository.LivroRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;


@Service
public class LivroService {
    private final LivroRepository livroRepository;

    public LivroService(LivroRepository livroRepository) {
        this.livroRepository = livroRepository;
    }

    public Livro buscarLivro(Long aLong){
        return livroRepository.findById(aLong).orElseThrow(()
        -> new ResponseStatusException(HttpStatus.NOT_FOUND,"livro não encontrado"));
    }

    public List<Livro> buscarTodosLivros(){
        return livroRepository.findAll();
    }

    public void deletarLivro(Long aLong){
        if(buscarLivroId(aLong)){
            livroRepository.deleteById(aLong);
        }else{
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "livro não existente");
        }
    }
    public Livro updateLivro(Livro livro, Long id){
        livro.setDisponivel(!livro.isDisponivel());
        livro.setId(id);
        if(buscarLivroId(livro.getId()) && !buscarLivroTitulo(livro.getTitulo())) {
            return livroRepository.save(livro);
        } else{
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "livro não existente ou titulo indisponivel");
        }
    }

    public Livro addLivro(Livro livro){
        if(buscarLivroTitulo(livro.getTitulo())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "livro ja existente");
        } else{
            return livroRepository.save(livro);
        }
    }

    public Boolean buscarLivroTitulo(String titulo){
        return livroRepository.existsByTitulo(titulo);
    }
    public Boolean buscarLivroId(Long id){
        return livroRepository.existsById(id);
    }
}
