package com.warlley.biblioteca.repository;

import com.warlley.biblioteca.model.Livro;
import org.jspecify.annotations.NullMarked;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LivroRepository extends JpaRepository<Livro, Long> {

    @Override
    @NullMarked
    Optional<Livro> findById(Long aLong);

    void deleteById(long id);
}
