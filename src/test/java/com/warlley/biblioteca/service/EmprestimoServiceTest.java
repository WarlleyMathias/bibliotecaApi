package com.warlley.biblioteca.service;

import com.warlley.biblioteca.dto.EmprestimoRequestDTO;
import com.warlley.biblioteca.dto.EmprestimoResponseDTO;
import com.warlley.biblioteca.model.Emprestimo;
import com.warlley.biblioteca.repository.EmprestimoRepository;
import com.warlley.biblioteca.util.DataUtil;
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
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class EmprestimoServiceTest {

    @Mock
    private EmprestimoRepository emprestimoRepository;

    @InjectMocks
    private EmprestimoService emprestimoService;

    @Nested
    @DisplayName("Tests do método buscar todos emprestimos.")
    class buscarTodosEmprestimos{

        @Test
        @DisplayName("Deve buscar todos os emprestimos do banco de dados e retornar uma lista.")
        void deveBuscarTodosEmprestimos(){
            Emprestimo emprestimo1 = new Emprestimo(1L,1L,1L,DataUtil.dataAtual(),DataUtil.dataDevolucao());
            Emprestimo emprestimo2 = new Emprestimo(2L,2L,2L,DataUtil.dataAtual(),DataUtil.dataDevolucao());
            List<Emprestimo> emprestimoList = List.of(emprestimo1,emprestimo2);

            when(emprestimoRepository.findAll()).thenReturn(emprestimoList);

            List<EmprestimoResponseDTO> resultado = emprestimoService.buscarTodosEmprestimo();

            assertNotNull(resultado);
            assertEquals(2,resultado.size());
            assertEquals(1,resultado.get(0).id());
            assertEquals(2,resultado.get(1).id());

            verify(emprestimoRepository,times(1)).findAll();
        }

        @Test
        @DisplayName("Deve lançar uma exceção ao tentar buscar todos os emprestimos do banco de dados.")
        void deveLancarExcecaoBuscarTodosEmprestimos(){
            when(emprestimoRepository.findAll()).thenThrow(new RuntimeException("Erro de conexão com o banco"));

            assertThrows(RuntimeException.class, () -> emprestimoService.buscarTodosEmprestimo());

            verify(emprestimoRepository, times(1)).findAll();
        }
    }

    @Nested
    @DisplayName("Tests do método adicionar emprestimo.")
    class addEmprestimo{

        @Test
        @DisplayName("Deve Salvar o emprestimo passado como parametro no banco de dados.")
        void deveSalvarEmprestimo(){
            EmprestimoRequestDTO dto = new EmprestimoRequestDTO(1L,1L);
            Emprestimo emprestimoSalvo = new Emprestimo(1L,1L,1L,DataUtil.dataAtual(),DataUtil.dataDevolucao());


            when(emprestimoRepository.existsByIdLivroAndIdUsuario(dto.id_livro(),dto.id_usuario())).thenReturn(false);
            when(emprestimoRepository.save(any(Emprestimo.class))).thenReturn(emprestimoSalvo);

            EmprestimoResponseDTO resultado = new EmprestimoResponseDTO(emprestimoRepository.save(emprestimoSalvo));

            assertNotNull(resultado);
            assertEquals(1L,resultado.id());
            assertEquals(1L,resultado.idLivro());
            assertEquals(1L,resultado.idUsuario());

            verify(emprestimoRepository,times(1)).existsByIdLivroAndIdUsuario(dto.id_livro(), dto.id_usuario());
            verify(emprestimoRepository, times(1)).save(emprestimoSalvo);
        }

        @Test
        @DisplayName("Deve lançar uma exceção 409 CONFLICT ao tentar salvar um novo emprestimo no banco de dados.")
        void deveLancarExcecaoSalvarEmprestimo(){
            EmprestimoRequestDTO dto = new EmprestimoRequestDTO(1L,1L);

            when(emprestimoRepository.existsByIdLivroAndIdUsuario(dto.id_livro(),dto.id_usuario())).thenReturn(true);

            ResponseStatusException ex =
                    assertThrows(ResponseStatusException.class, () -> emprestimoService.addEmprestimo(dto));

            assertEquals(409, ex.getStatusCode().value());

            verify(emprestimoRepository,times(1)).existsByIdLivroAndIdUsuario(dto.id_livro(), dto.id_usuario());
            verify(emprestimoRepository, never()).save(any(Emprestimo.class));
        }

    }

    @Nested
    @DisplayName("Tests do método remove emprestimo.")
    class removeEmprestimo{

        @Test
        @DisplayName("Deve Remove do banco de dados emprestimo pelo Id passado como parametro.")
        void deveRemoveEmprestimo(){
            Long id = 1L;
            Emprestimo emprestimoDeletado = new Emprestimo(1L,1L,1L,DataUtil.dataAtual(),DataUtil.dataDevolucao());

            when(emprestimoRepository.existsById(id)).thenReturn(true);
            doNothing().when(emprestimoRepository).delete(emprestimoDeletado);

            emprestimoService.removeEmprestimo(id);

            verify(emprestimoRepository,times(1)).existsById(id);
            verify(emprestimoRepository,times(1)).deleteById(id);

        }

        @Test
        @DisplayName("Deve lançar uma exceção 404 ao tentar remover o emprestimo")
        void deveLancarExcecaoRemoveEmprestimo(){
            Long id = 1L;

            when(emprestimoRepository.existsById(id)).thenReturn(false);

            ResponseStatusException ex =
                    assertThrows(ResponseStatusException.class, () -> emprestimoService.removeEmprestimo(id));

            assertEquals(404,ex.getStatusCode().value());
            verify(emprestimoRepository, times(1)).existsById(id);
            verify(emprestimoRepository, never()).deleteById(anyLong());
        }
    }
}
