package com.warlley.biblioteca.service;

import com.warlley.biblioteca.dto.LivroRequestDTO;
import com.warlley.biblioteca.dto.LivroResponseDTO;
import com.warlley.biblioteca.model.Livro;
import com.warlley.biblioteca.repository.LivroRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class LivroServiceTest {

    @Mock
    private LivroRepository livroRepository;

    @InjectMocks
    private LivroService livroService;

    @Nested
    @DisplayName("Tests do método addLivro.")
    class addLivroTest{

        @Test
        @DisplayName("Deve salvar o livro com sucesso, quando o titulo não existir.")
        void deveSalvarLivro(){
            LivroRequestDTO dto = new LivroRequestDTO("Clean Code", "Robert C. Martin",1999);
            Livro livroSalvo = new Livro(1L, "Clean Code", "Robert C. Martin",1999, true);

            when(livroRepository.existsByTitulo(dto.titulo())).thenReturn(false);
            when(livroRepository.save(any(Livro.class))).thenReturn(livroSalvo);

            LivroResponseDTO resultado = livroService.addLivro(dto);

            assertNotNull(resultado);
            assertEquals(1L,resultado.id());
            assertEquals("Clean Code",resultado.titulo());

            verify(livroRepository, times(1)).existsByTitulo(dto.titulo());
            verify(livroRepository, times(1)).save(any(Livro.class));
        }

        @Test
        @DisplayName("Deve lançar uma exceção, se o titulo do livro a ser cadastrado for igual a um livro já existente no banco de dados.")
        void deveLancarExcecaoTituloLivroExiste(){
            LivroRequestDTO dto = new LivroRequestDTO("Clean Code", "Robert C. Martin",1999);

            when(livroRepository.existsByTitulo(dto.titulo())).thenReturn(true);

            ResponseStatusException ex =
                    assertThrows(ResponseStatusException.class, () -> livroService.addLivro(dto));

            assertEquals(409, ex.getStatusCode().value());
            verify(livroRepository, times(1)).existsByTitulo(dto.titulo());
            verify(livroRepository, never()).save(any(Livro.class));
        }
    }

}
