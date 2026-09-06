package com.warlley.biblioteca.model;

import com.warlley.biblioteca.dto.EmprestimoRequestDTO;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "emprestimos")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
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
}
