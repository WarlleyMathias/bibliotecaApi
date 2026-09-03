package com.warlley.biblioteca.repository;

import com.warlley.biblioteca.model.Emprestimo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmprestimoRepository extends JpaRepository<Emprestimo, Long> {
    boolean existsByIdLivroAndIdUsuario(Long idLivro, Long idUsuario);
}
