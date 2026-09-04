package com.warlley.biblioteca.service;

import com.warlley.biblioteca.dto.EmprestimoRequestDTO;
import com.warlley.biblioteca.dto.EmprestimoResponseDTO;
import com.warlley.biblioteca.model.Emprestimo;
import com.warlley.biblioteca.repository.EmprestimoRepository;
import com.warlley.biblioteca.util.DataUtil;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class EmprestimoService {
    private final EmprestimoRepository emprestimoRepository;
    private final LivroService livroService;

    public EmprestimoService(EmprestimoRepository emprestimoRepository, LivroService livroService){
        this.emprestimoRepository = emprestimoRepository;
        this.livroService = livroService;
    }

    public Emprestimo buscarEmprestimo(Long id){
        return emprestimoRepository.findById(id).orElseThrow(()
        -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Emprestimo não existente!"));
    }

    @Transactional
    public EmprestimoResponseDTO addEmprestimo(EmprestimoRequestDTO emprestimoDTO){
        Emprestimo emprestimo = new Emprestimo(emprestimoDTO);
        if(verificaEmprestimo(emprestimo.getIdLivro(),emprestimo.getIdUsuario())){
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Livro já emprestado!");
        }else{
            emprestimo.setDataDevolucao(DataUtil.dataDevolucao());
            emprestimo.setDataEmprestimo(DataUtil.dataAtual());
            livroService.disponivelLivro(emprestimo.getIdLivro());
            return new EmprestimoResponseDTO(emprestimoRepository.save(emprestimo));
        }
    }
    @Transactional
    public void removeEmprestimo(Long id){
        livroService.disponivelLivro(buscarEmprestimo(id).getIdLivro());
        emprestimoRepository.delete(buscarEmprestimo(id));
    }

    public Boolean verificaEmprestimo(Long idLivro, Long idUsuario){
        return emprestimoRepository.existsByIdLivroAndIdUsuario(idLivro, idUsuario);
    }

    public List<EmprestimoResponseDTO> buscarTodosEmprestimo(){
        return emprestimoRepository.findAll().stream().map(EmprestimoResponseDTO::new).toList();
    }
}
