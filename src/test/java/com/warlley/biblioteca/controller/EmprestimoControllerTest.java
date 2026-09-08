package com.warlley.biblioteca.controller;

import com.warlley.biblioteca.dto.EmprestimoRequestDTO;
import com.warlley.biblioteca.dto.EmprestimoResponseDTO;
import com.warlley.biblioteca.service.EmprestimoService;
import com.warlley.biblioteca.util.DataUtil;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.server.ResponseStatusException;
import tools.jackson.databind.ObjectMapper;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest
class EmprestimoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private EmprestimoService emprestimoService;

    @Autowired
    private ObjectMapper objectMapper;

    @Nested
    @DisplayName("GET /emprestimos - Buscar Emprestimos")
    class getEmprestimos{

        @Test
        @DisplayName("Deve Retornar status 200 OK e a lista de emprestimos.")
        void deveBuscarEmprestimos() throws Exception{
            EmprestimoResponseDTO emprestimo1 = new EmprestimoResponseDTO(1L,1L,1L, DataUtil.dataAtual(),DataUtil.dataDevolucao());
            EmprestimoResponseDTO emprestimo2 = new EmprestimoResponseDTO(2L,2L,2L, DataUtil.dataAtual(),DataUtil.dataDevolucao());
            List<EmprestimoResponseDTO> emprestimoResponseDTOList = List.of(emprestimo1,emprestimo2);

            when(emprestimoService.buscarTodosEmprestimo()).thenReturn(emprestimoResponseDTOList);

            mockMvc.perform(get("/emprestimos")
                    .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.length()").value(2))
                    .andExpect(jsonPath("$[0].id").value(1))
                    .andExpect(jsonPath("$[0].idUsuario").value(1))
                    .andExpect(jsonPath("$[0].idLivro").value(1))
                    .andExpect(jsonPath("$[1].id").value(2))
                    .andExpect(jsonPath("$[1].idUsuario").value(2))
                    .andExpect(jsonPath("$[1].idLivro").value(2));

            verify(emprestimoService,times(1)).buscarTodosEmprestimo();
        }
        @Test
        @DisplayName("Deve retornar status 200 OK e uma lista vazia quando não houver emprestimos.")
        void deveLancarExcecaoBuscarEmprestimos() throws Exception{
            List<EmprestimoResponseDTO> listaVazia = new ArrayList<>();

            when(emprestimoService.buscarTodosEmprestimo()).thenReturn(listaVazia);

            mockMvc.perform(get("/emprestimos")
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.length()").value(0));

            verify(emprestimoService,times(1)).buscarTodosEmprestimo();
        }
    }
    @Nested
    @DisplayName("DELETE /emprestimos/{id}/devolucao - Devolver Emprestimo.")
    class devolucao{

        @Test
        @DisplayName("Deve Retornar status 204 quando devolucao de emprestimo efetuada com sucesso.")
        void deveDevolverEmprestimo() throws Exception{
            Long id = 1L;

            doNothing().when(emprestimoService).removeEmprestimo(id);

            mockMvc.perform(delete("/emprestimos/{id}/devolucao",id)
                    .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isNoContent());

            verify(emprestimoService, times(1)).removeEmprestimo(id);
        }
        @Test
        @DisplayName("Deve Retornar status 409 quando devolucao bloquear, porque o emprestimo está sendo usado por outra tabela.")
        void deveLancarExcecaoConflitoDevolverEmprestimo() throws Exception{
            Long id = 1L;

            doThrow(new ResponseStatusException(HttpStatus.CONFLICT, "Empréstimo vinculado a outra entidade"))
                    .when(emprestimoService).removeEmprestimo(id);

            mockMvc.perform(delete("/emprestimos/{id}/devolucao",id)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isConflict());

            verify(emprestimoService, times(1)).removeEmprestimo(id);
        }
        @Test
        @DisplayName("Deve Retornar status 404 quando emprestimo não existe, para ser deletado.")
        void deveLancarExcecaoNaoExisteDevolverEmprestimo() throws Exception{
            Long id = 1L;

            doThrow(new ResponseStatusException(HttpStatus.NOT_FOUND, "Empréstimo vinculado a outra entidade"))
                    .when(emprestimoService).removeEmprestimo(id);

            mockMvc.perform(delete("/emprestimos/{id}/devolucao",id)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isNotFound());

            verify(emprestimoService, times(1)).removeEmprestimo(id);
        }
    }
    @Nested
    @DisplayName("POST /emprestimos/add - Criar Emprestimo")
    class addEmprestimo {

        @Test
        @DisplayName("Deve Retornar status 201 quando emprestimo efetuado com sucesso.")
        void deveCriarEmprestimo() throws Exception{
            EmprestimoRequestDTO emprestimoRequest = new EmprestimoRequestDTO(1L,1L);
            EmprestimoResponseDTO emprestimoSalvo = new EmprestimoResponseDTO(1L,1L,1L, DataUtil.dataAtual(),DataUtil.dataDevolucao());

            when(emprestimoService.addEmprestimo(emprestimoRequest)).thenReturn(emprestimoSalvo);

            mockMvc.perform(post("/emprestimos/add")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(emprestimoRequest)))
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.idUsuario").value(1))
                    .andExpect(jsonPath("$.idLivro").value(1));

            verify(emprestimoService,times(1)).addEmprestimo(emprestimoRequest);
        }

        @Test
        @DisplayName("Deve Retornar status 409 quando livro já está emprestado.")
        void deveLancarExcecaoConflitoCriarEmprestimo() throws Exception{
            EmprestimoRequestDTO emprestimoRequest = new EmprestimoRequestDTO(1L,1L);

            when(emprestimoService.addEmprestimo(emprestimoRequest))
                    .thenThrow(new ResponseStatusException(HttpStatus.CONFLICT, "Conflito: Livro já emprestado."));

            mockMvc.perform(post("/emprestimos/add")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(emprestimoRequest)))
                    .andExpect(status().isConflict());

            verify(emprestimoService,times(1)).addEmprestimo(emprestimoRequest);
        }
    }
}
