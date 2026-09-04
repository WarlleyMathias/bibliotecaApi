package com.warlley.biblioteca.model;

import com.warlley.biblioteca.dto.UsuarioRequestDTO;
import jakarta.persistence.*;

@Entity
@Table(name = "usuarios")
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
    public Usuario(){
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public String getEmail() {
        return email;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
