package com.warlley.biblioteca.service;

import com.warlley.biblioteca.dto.LivroRequestDTO;
import com.warlley.biblioteca.dto.LivroResponseDTO;
import com.warlley.biblioteca.model.Livro;
import com.warlley.biblioteca.repository.LivroRepository;
import jakarta.transaction.Transactional;
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

    public LivroResponseDTO buscarLivro(Long aLong){
        return new LivroResponseDTO(livroRepository.findById(aLong).orElseThrow(()
        -> new ResponseStatusException(HttpStatus.NOT_FOUND,"livro não encontrado")));
    }
    public void disponivelLivro(Long id){
        Livro livro = livroRepository.findById(id).orElseThrow(()
                -> new ResponseStatusException(HttpStatus.NOT_FOUND,"livro não encontrado"));
        livro.setDisponivel(!livro.isDisponivel());
        livroRepository.save(livro);
    }

    public List<LivroResponseDTO> buscarTodosLivros(){
        return livroRepository.findAll().stream().map(LivroResponseDTO::new).toList();
    }

    public void deletarLivro(Long aLong){
        if(buscarLivroId(aLong)){
            livroRepository.deleteById(aLong);
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "livro não existente");

    }
    @Transactional
    public LivroResponseDTO updateLivro(LivroRequestDTO livroDTO, Long id){
        Livro livro = new Livro(livroDTO);
        livro.setDisponivel(!livro.isDisponivel());
        livro.setId(id);
        if(buscarLivroId(livro.getId())) {
            if(!buscarLivroTitulo(livro.getTitulo())) {
                return new LivroResponseDTO(livroRepository.save(livro));
            }
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Titulo indisponivel.");
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "livro não existente.");

    }

    @Transactional
    public LivroResponseDTO addLivro(LivroRequestDTO livroDTO){
        if(buscarLivroTitulo(livroDTO.titulo())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "livro ja existente");
        }
        Livro livro = new Livro(livroDTO);
        return new LivroResponseDTO(livroRepository.save(livro));

    }

    public Boolean buscarLivroTitulo(String titulo){
        return livroRepository.existsByTitulo(titulo);
    }
    public Boolean buscarLivroId(Long id){
        return livroRepository.existsById(id);
    }
}
