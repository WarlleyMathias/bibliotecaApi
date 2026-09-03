package com.warlley.biblioteca.model;

import jakarta.persistence.*;

@Entity
@Table(name = "emprestimos")
public class Emprestimo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long idLivro;
    private Long idUsuario;
    private String dataEmprestimo;
    private String dataDevolucao;

    public Long getIdLivro() {
        return idLivro;
    }

    public Long getIdUsuario() {
        return idUsuario;
    }
    

    public void setDataEmprestimo(String dataEmprestimo) {
        this.dataEmprestimo = dataEmprestimo;
    }

    public void setDataDevolucao(String dataDevolucao) {
        this.dataDevolucao = dataDevolucao;
    }
}
