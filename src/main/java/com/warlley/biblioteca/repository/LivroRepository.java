package com.warlley.biblioteca.repository;

import com.warlley.biblioteca.model.Livro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface LivroRepository extends JpaRepository<Livro, Long> {

    Boolean findLivroByTitulo(String titulo);

    Boolean findLivroById(long id);
}
