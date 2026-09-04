package com.warlley.biblioteca.model;

import com.warlley.biblioteca.dto.EmprestimoRequestDTO;
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

    public Emprestimo(EmprestimoRequestDTO emprestimoRequestDTO){
        this.idLivro = emprestimoRequestDTO.id_livro();
        this.idUsuario = emprestimoRequestDTO.id_usuario();
    }

    public Emprestimo(){
    }

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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setIdLivro(Long idLivro) {
        this.idLivro = idLivro;
    }

    public void setIdUsuario(Long idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getDataEmprestimo() {
        return dataEmprestimo;
    }

    public String getDataDevolucao() {
        return dataDevolucao;
    }
}
