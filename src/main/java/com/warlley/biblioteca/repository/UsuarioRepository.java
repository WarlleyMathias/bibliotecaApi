package com.warlley.biblioteca.repository;

import com.warlley.biblioteca.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario,Long>{
    boolean existsByNome(String nome);
    boolean existsByEmail(String Email);
}
