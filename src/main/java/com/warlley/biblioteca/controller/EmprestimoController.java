package com.warlley.biblioteca.controller;

import com.warlley.biblioteca.dto.EmprestimoRequestDTO;
import com.warlley.biblioteca.dto.EmprestimoResponseDTO;
import com.warlley.biblioteca.service.EmprestimoService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class EmprestimoController {
    private final EmprestimoService emprestimoService;

    public EmprestimoController(EmprestimoService emprestimoService){
        this.emprestimoService = emprestimoService;
    }

    @GetMapping("/emprestimos")
    public List<EmprestimoResponseDTO> getEmprestimo(){
        return emprestimoService.buscarTodosEmprestimo();
    }

    @PostMapping("/emprestimos/{id}/devolucao")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void devolucao(@PathVariable Long id){
        emprestimoService.removeEmprestimo(id);
    }

    @PostMapping("/emprestimos/add")
    @ResponseStatus(HttpStatus.CREATED)
    public EmprestimoResponseDTO addEmprestimo(@RequestBody EmprestimoRequestDTO emprestimoDTO){
        return emprestimoService.addEmprestimo(emprestimoDTO);
    }
}
