package com.warlley.biblioteca.service;

import com.warlley.biblioteca.model.Emprestimo;
import com.warlley.biblioteca.repository.EmprestimoRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class EmprestimoService {
    private final EmprestimoRepository emprestimoRepository;

    public EmprestimoService(EmprestimoRepository emprestimoRepository){
        this.emprestimoRepository = emprestimoRepository;
    }

    public Emprestimo buscarEmprestimo(Long id){
        return emprestimoRepository.findById(id).orElseThrow(()
        -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Emprestimo não existente!"));
    }

    public Emprestimo addEmprestimo(Emprestimo emprestimo){
        if(verificaEmprestimo(emprestimo.getIdLivro(),emprestimo.getIdUsuario())){
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Livro já emprestado!");
        }else{
            return emprestimoRepository.save(emprestimo);
        }
    }
    public void removeEmprestimo(Long id){
        emprestimoRepository.delete(buscarEmprestimo(id));
    }

    public Boolean verificaEmprestimo(Long idLivro, Long idUsuario){
        return emprestimoRepository.existsByIdLivroAndIdUsuario(idLivro, idUsuario);
    }

    public List<Emprestimo> buscarTodosEmprestimo(){
        return emprestimoRepository.findAll();
    }
}
