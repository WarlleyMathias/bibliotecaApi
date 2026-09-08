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

import java.util.List;
import java.util.Optional;

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
    @DisplayName("Tests de método updateLivro.")
    class updateLivro{

        @Test
        @DisplayName("Deve retorna um livro apos atualizar dados do livro no banco de dados com sucesso.")
        void deveAtualizarLivro(){
            Long id = 1L;
            LivroRequestDTO livro = new LivroRequestDTO("Code Clean","Warlley",1999);
            Livro livroSalvo = new Livro(1L, "Clean Code", "Warlley",1999, true);

            when(livroRepository.existsByTitulo(livro.titulo())).thenReturn(true);
            when(livroRepository.existsById(id)).thenReturn(true);
            when(livroRepository.save(any(Livro.class))).thenReturn(livroSalvo);

            LivroResponseDTO resultado = livroService.updateLivro(livro, id);

            assertNotNull(resultado);
            assertEquals(1L,resultado.id());
            assertEquals("Code Clean",resultado.titulo());
            assertEquals("Warlley",resultado.autor());
            assertEquals(1999,resultado.ano());
            assertTrue(resultado.disponivel());

            verify(livroRepository, times(1)).existsByTitulo(livro.titulo());
            verify(livroRepository, times(1)).existsById(id);
            verify(livroRepository, times(1)).save(livroSalvo);
        }
        @Test
        @DisplayName("Deve Lançar uma exceção 404 NOT_FOUND ao tentar atualizar livro no banco de dados.")
        void deveLancarUmaExcecaoIdNaoEncontrado(){
            Long id = 1L;
            LivroRequestDTO livro = new LivroRequestDTO("Code Clean","Warlley",1999);

            when(livroRepository.existsById(id)).thenReturn(false);

            ResponseStatusException ex =
                    assertThrows(ResponseStatusException.class, () -> livroService.updateLivro(livro,id));

            assertEquals(404,ex.getStatusCode().value());
            verify(livroRepository, times(1)).existsById(id);
            verify(livroRepository, never()).save(any(Livro.class));

        }
        @Test
        @DisplayName("Deve Lançar uma exceção 409 CONFLICT ao tentar atualizar livro no banco de dados.")
        void deveLancarUmaExcecaoTituloConflito(){
            Long id = 1L;
            LivroRequestDTO livro = new LivroRequestDTO("Code Clean","Warlley",1999);

            when(livroRepository.existsByTitulo(livro.titulo())).thenReturn(false);

            ResponseStatusException ex =
                    assertThrows(ResponseStatusException.class, () -> livroService.updateLivro(livro,id));

            assertEquals(409,ex.getStatusCode().value());
            verify(livroRepository, times(1)).existsByTitulo(livro.titulo());
            verify(livroRepository, never()).save(any(Livro.class));

        }

    }

    @Nested
    @DisplayName("Tests de método deletarLivro.")
    class deletarLivroTest{

        @Test
        @DisplayName("Recebe um Id como parametro para deletar um livro do banco de bados com sucesso.")
        void deveDeletarLivro(){
            Long id = 1L;

            when(livroRepository.existsById(id)).thenReturn(true);

            doNothing().when(livroRepository).deleteById(id);

            livroService.deletarLivro(id);

            verify(livroRepository,times(1)).existsById(id);
            verify(livroRepository,times(1)).deleteById(id);
        }
        @Test
        @DisplayName("Deve Lancar uma exceção 404 NOT_FOUND ao tentar deletar um livro através do Id.")
        void deveLancarExcecaoDeletarLivro(){
            Long id = 1L;

            when(livroRepository.existsById(id)).thenReturn(false);

            ResponseStatusException ex =
                    assertThrows(ResponseStatusException.class, () -> livroService.deletarLivro(id));

            assertEquals(404,ex.getStatusCode().value());
            verify(livroRepository, times(1)).existsById(id);
            verify(livroRepository, never()).deleteById(anyLong());

        }
    }

    @Nested
    @DisplayName("Tests do método buscarLivro.")
    class buscarLivroTest{

        @Test
        @DisplayName("Deve buscar o livro, com o id.")
        void deveBuscarLivro(){
            Livro livroSalvo = new Livro(1L, "Clean Code", "Robert C. Martin",1999, true);

            when(livroRepository.findById(livroSalvo.getId())).thenReturn(Optional.of(livroSalvo));

            LivroResponseDTO resultado = livroService.buscarLivro(livroSalvo.getId());

            assertNotNull(resultado);
            assertEquals(1L,resultado.id());
            assertEquals("Clean Code", resultado.titulo());
            assertEquals("Robert C. Martin", resultado.autor());
            assertEquals(1999, resultado.ano());
            assertTrue(resultado.disponivel());

            verify(livroRepository, times(1)).findById(livroSalvo.getId());
        }

        @Test
        @DisplayName("Deve lançar exceção 404 NOT_FOUND ao tentar buscar livro por ID inexistente.")
        void deveLancarUmaExcecaoBuscarLivro(){
            Long id = 1L;

            when(livroRepository.findById(id)).thenReturn(Optional.empty());

            ResponseStatusException ex =
                    assertThrows(ResponseStatusException.class, () -> livroService.buscarLivro(1L));

            assertEquals(404, ex.getStatusCode().value());
            verify(livroRepository, times(1)).findById(1L);
        }

    }
    @Nested
    @DisplayName("Tests do método buscarTodosLivro.")
    class buscarListaLivroTest{

        @Test
        @DisplayName("Deve buscar todos os livros.")
        void deveBuscarTodosLivros(){
            Livro livro1 = new Livro(1L, "Clean Code", "Robert C. Martin", 1999, true);
            Livro livro2 = new Livro(2L, "Domain-Driven Design", "Eric Evans", 2003, true);
            List<Livro> listaLivros = List.of(livro1, livro2);
            when(livroRepository.findAll()).thenReturn(listaLivros);

            List<LivroResponseDTO> resultado = livroService.buscarTodosLivros();

            assertNotNull(resultado);
            assertEquals(2, resultado.size());
            assertEquals("Clean Code", resultado.get(0).titulo());
            assertEquals("Domain-Driven Design", resultado.get(1).titulo());

            verify(livroRepository, times(1)).findAll();
        }

        @Test
        @DisplayName("Deve lançar exceção tentar buscar lista de livros.")
        void deveLancarUmaExcecaoBuscarLivro(){
            when(livroRepository.findAll()).thenThrow(new RuntimeException("Erro de conexão com o banco"));

            assertThrows(RuntimeException.class, () -> livroService.buscarTodosLivros());

            verify(livroRepository, times(1)).findAll();
        }

    }

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
        @DisplayName("Deve lançar uma exceção 409 CONFLICT, se o titulo do livro a ser cadastrado for igual a um livro já existente no banco de dados.")
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
