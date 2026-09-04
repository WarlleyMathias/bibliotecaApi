package com.warlley.biblioteca.dto;

import com.warlley.biblioteca.model.Emprestimo;

public record EmprestimoResponseDTO(
        Long id,
        Long idUsuario,
        Long idLivro,
        String dataEmprestimo,
        String dataDevolucao
) {
    public EmprestimoResponseDTO(Emprestimo emprestimo){
        this(emprestimo.getId(), emprestimo.getIdUsuario(),
                emprestimo.getIdLivro(), emprestimo.getDataEmprestimo(), emprestimo.getDataDevolucao());
    }
}
