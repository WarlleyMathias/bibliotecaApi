package com.warlley.biblioteca.model;

import com.warlley.biblioteca.dto.UsuarioRequestDTO;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "usuarios")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Usuario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String nome;
    private String email;

    public Usuario(UsuarioRequestDTO usuarioRequestDTO){
        this.nome = usuarioRequestDTO.nome();
        this.email = usuarioRequestDTO.email();
    }
}
