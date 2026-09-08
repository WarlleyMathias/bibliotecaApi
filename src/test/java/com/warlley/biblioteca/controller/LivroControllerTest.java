package com.warlley.biblioteca.controller;


import com.warlley.biblioteca.dto.LivroRequestDTO;
import com.warlley.biblioteca.dto.LivroResponseDTO;
import com.warlley.biblioteca.service.LivroService;
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
class LivroControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private LivroService livroService;

    @Autowired
    private ObjectMapper objectMapper;

    @Nested
    @DisplayName("GET /livros - buscar todos os livros.")
    class listarLivros {

        @Test
        @DisplayName("Deve Retornar status 200 OK e a lista de livros.")
        void deveListarLivros() throws Exception {
            LivroResponseDTO livro1 = new LivroResponseDTO(1L, "Mundo", "Marcos", 1999, true);
            LivroResponseDTO livro2 = new LivroResponseDTO(1L, "Mundo2", "Marcola", 1999, true);
            List<LivroResponseDTO> livroResponseDTOList = List.of(livro1, livro2);

            when(livroService.buscarTodosLivros()).thenReturn(livroResponseDTOList);

            mockMvc.perform(get("/livros")
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.length()").value(2))
                    .andExpect(jsonPath("$[0].id").value(1))
                    .andExpect(jsonPath("$[0].titulo").value("Mundo"))
                    .andExpect(jsonPath("$[0].autor").value("Marcos"))
                    .andExpect(jsonPath("$[0].ano").value(1999))
                    .andExpect(jsonPath("$[0].disponivel").value(true))
                    .andExpect(jsonPath("$[1].id").value(2))
                    .andExpect(jsonPath("$[1].titulo").value("Mundo2"));

            verify(livroService, times(1)).buscarTodosLivros();
        }

        @Test
        @DisplayName("Deve retornar status 200 OK e uma lista vazia quando não houver livros.")
        void deveLancarExcecaoListarLivros() throws Exception {
            List<LivroResponseDTO> livroResponseDTOList = new ArrayList<>();

            when(livroService.buscarTodosLivros()).thenReturn(livroResponseDTOList);

            mockMvc.perform(get("/livros")
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.length()").value(0));

            verify(livroService, times(1)).buscarTodosLivros();
        }

        @Nested
        @DisplayName("GET /livros/{id} - buscar livro.")
        class getLivro {

            @Test
            @DisplayName("Deve Retornar status 200 OK e livro.")
            void deveBuscarLivro() throws Exception {
                Long id = 1L;
                LivroResponseDTO livroBuscado = new LivroResponseDTO(1L, "Mundo", "Marcos", 1999, true);

                when(livroService.buscarLivro(id)).thenReturn(livroBuscado);

                mockMvc.perform(get("/livros/{id}",id)
                                .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isOk())
                        .andExpect(jsonPath("$.id").value(1))
                        .andExpect(jsonPath("$.titulo").value("Mundo"))
                        .andExpect(jsonPath("$.autor").value("Marcos"))
                        .andExpect(jsonPath("$.ano").value(1999))
                        .andExpect(jsonPath("$.disponivel").value(true));

                verify(livroService, times(1)).buscarLivro(id);
            }

            @Test
            @DisplayName("Deve Retornar status 404 quando livro não existir.")
            void deveLancarExcecaoBuscarLivro() throws Exception {
                Long id = 1L;

                when(livroService.buscarLivro(id)).thenThrow(new ResponseStatusException(HttpStatus.NOT_FOUND, "Não existe Livro com esse Id"));

                mockMvc.perform(get("/livros/{id}",id)
                                .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isNotFound());

                verify(livroService, times(1)).buscarLivro(id);
            }
        }

        @Nested
        @DisplayName("POST /livros - criar livro.")
        class addLivro {

            @Test
            @DisplayName("Deve Retornar status 201 quando livro criado com sucesso.")
            void deveCriarLivro() throws Exception {
                LivroRequestDTO livroRequestDTO = new LivroRequestDTO("Mundo", "Marcos", 1999);
                LivroResponseDTO livroSalvo = new LivroResponseDTO(1L, "Mundo", "Marcos", 1999, true);

                when(livroService.addLivro(livroRequestDTO)).thenReturn(livroSalvo);

                mockMvc.perform(post("/livros")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(livroRequestDTO)))
                        .andExpect(status().isCreated())
                        .andExpect(jsonPath("$.titulo").value("Mundo"))
                        .andExpect(jsonPath("$.autor").value("Marcos"))
                        .andExpect(jsonPath("$.ano").value(1999));

                verify(livroService, times(1)).addLivro(livroRequestDTO);
            }

            @Test
            @DisplayName("Deve retornar status 400 Bad Request quando parâmetros for inválido.")
            void deveLancarExcecaoEntradaInvalidaCriarLivro() throws Exception {

                LivroRequestDTO livroRequestDTO = new LivroRequestDTO("", "Marcos", 1999);

                mockMvc.perform(post("/livros")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(livroRequestDTO)))
                        .andExpect(status().isBadRequest());

                verify(livroService, never()).addLivro(any(LivroRequestDTO.class));

            }

            @Test
            @DisplayName("Deve Retornar status 409 quando livro já existe.")
            void deveLancarExcecaoConflitoCriarLivro() throws Exception {
                LivroRequestDTO livroRequestDTO = new LivroRequestDTO("Mundo", "Marcos", 1999);

                when(livroService.addLivro(livroRequestDTO)).thenThrow(new ResponseStatusException(HttpStatus.CONFLICT, "Já existe um livro com esse titulo."));

                mockMvc.perform(post("/livros")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(livroRequestDTO)))
                        .andExpect(status().isConflict());

                verify(livroService, times(1)).addLivro(livroRequestDTO);
            }
        }

        @Nested
        @DisplayName("PUT /livros/{id} - atualizar livro.")
        class updateLivro {

            @Test
            @DisplayName("Deve Retornar status 200 OK quando livro atualizado com sucesso.")
            void deveAtualizarLivro() throws Exception {
                Long id = 1L;
                LivroRequestDTO livroRequestDTO = new LivroRequestDTO("Mundo", "Marcos", 1999);
                LivroResponseDTO livroAtualizado = new LivroResponseDTO(1L, "Mundo", "Marcos", 1999, true);

                when(livroService.updateLivro(livroRequestDTO, id)).thenReturn(livroAtualizado);

                mockMvc.perform(put("/livros/{id}",id)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(livroRequestDTO)))
                        .andExpect(status().isOk())
                        .andExpect(jsonPath("$.titulo").value("Mundo"))
                        .andExpect(jsonPath("$.autor").value("Marcos"))
                        .andExpect(jsonPath("$.ano").value(1999));

                verify(livroService, times(1)).updateLivro(livroRequestDTO, id);
            }

            @Test
            @DisplayName("Deve Retornar status 404 quando livro não existir.")
            void deveLancarExcecaoAtualizarLivro() throws Exception {
                Long id = 1L;
                LivroRequestDTO livroRequestDTO = new LivroRequestDTO("Mundo", "Marcos", 1999);

                when(livroService.updateLivro(livroRequestDTO, id)).thenThrow(new ResponseStatusException(HttpStatus.NOT_FOUND, "Livro não existe para sem atualizado."));

                mockMvc.perform(put("/livros/{id}",id)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(livroRequestDTO)))
                        .andExpect(status().isNotFound());

                verify(livroService, times(1)).updateLivro(livroRequestDTO, id);
            }

            @Test
            @DisplayName("Deve retornar status 400 Bad Request quando parâmetros for inválido.")
            void deveLancarExcecaoEntradaInvalidaAtualizarLivro() throws Exception {
                Long id = 1L;
                LivroRequestDTO livroRequestDTO = new LivroRequestDTO("", "Marcos", 1999);

                mockMvc.perform(put("/livros/{id}",id)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(livroRequestDTO)))
                        .andExpect(status().isBadRequest());

                verify(livroService, never()).updateLivro(livroRequestDTO, id);
            }

        }

        @Test
        @DisplayName("Deve Retornar status 409 quando livro já existe com esse nome.")
        void deveLancarExcecaoConflitoAtualizarLivro() throws Exception {
            Long id = 1L;
            LivroRequestDTO livroRequestDTO = new LivroRequestDTO("Mundo", "Marcos", 1999);

            when(livroService.updateLivro(livroRequestDTO, id)).thenThrow(new ResponseStatusException(HttpStatus.CONFLICT, "Já existe um livro com esse nome."));

            mockMvc.perform(put("/livros/{id}",id)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(livroRequestDTO)))
                    .andExpect(status().isConflict());

            verify(livroService, times(1)).updateLivro(livroRequestDTO, id);
        }

    }

    @Nested
    @DisplayName("DELETE /livros/{id} - atualizar livro.")
    class deleteLivro {

        @Test
        @DisplayName("Deve Retornar status 204 quando livro deletado com sucesso.")
        void deveAtualizarLivro() throws Exception {
            Long id = 1L;

            doNothing().when(livroService).deletarLivro(id);

            mockMvc.perform(delete("/livros/{id}",id)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isNoContent());

            verify(livroService, times(1)).deletarLivro(id);
        }

        @Test
        @DisplayName("Deve Retornar status 404 quando livro não existe, para ser deletado.")
        void deveLancarExcecaoEntradaInvalidaAtualizarLivro() throws Exception {
            Long id = 1L;

            doThrow(new ResponseStatusException(HttpStatus.NOT_FOUND, "Não existe Livro com esse Id."))
                    .when(livroService).deletarLivro(id);

            mockMvc.perform(delete("/livros/{id}",id)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isNotFound());

            verify(livroService, times(1)).deletarLivro(id);
        }

        @Test
        @DisplayName("Deve Retornar status 409 quando livro está sendo usado por outra tabela.")
        void deveLancarExcecaoConflitoAtualizarLivro() throws Exception {
            Long id = 1L;

            doThrow(new ResponseStatusException(HttpStatus.CONFLICT, "Não existe Livro com esse Id."))
                    .when(livroService).deletarLivro(id);

            mockMvc.perform(delete("/livros/{id}",id)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isConflict());

            verify(livroService, times(1)).deletarLivro(id);
        }
    }
}

