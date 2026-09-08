package com.warlley.biblioteca.model;

import com.warlley.biblioteca.dto.LivroRequestDTO;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "livros")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Livro {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String titulo;
    private String autor;
    private int ano;
    private boolean disponivel;

    public Livro(LivroRequestDTO livroRequestDTO) {
        this.titulo = livroRequestDTO.titulo();
        this.autor = livroRequestDTO.autor();
        this.ano = livroRequestDTO.ano();
        this.disponivel = true;
    }
}
