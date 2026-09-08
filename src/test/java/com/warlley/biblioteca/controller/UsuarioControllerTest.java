package com.warlley.biblioteca.controller;

import com.warlley.biblioteca.dto.UsuarioRequestDTO;
import com.warlley.biblioteca.dto.UsuarioResponseDTO;
import com.warlley.biblioteca.service.UsuarioService;
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

import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UsuarioController.class)
class UsuarioControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UsuarioService usuarioService;

    @Autowired
    private ObjectMapper objectMapper;

    @Nested
    @DisplayName("POST /usuarios - Cadastrar usuario.")
    class salvarUsuario {

        @Test
        @DisplayName("Deve retornar status 201 Created quando o DTO for válido.")
        void deveSalvarUsuario() throws Exception{
            UsuarioRequestDTO usuarioRequestDTO = new UsuarioRequestDTO("Marcelo","marcelo@gmail.com");
            UsuarioResponseDTO usuarioResponseDTO = new UsuarioResponseDTO(1L,"Marcelo","marcelo@gmail.com");

            when(usuarioService.criarUsuario(usuarioRequestDTO)).thenReturn(usuarioResponseDTO);

            mockMvc.perform(post("/usuarios")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(usuarioRequestDTO)))
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.id").value(1))
                    .andExpect(jsonPath("$.nome").value("Marcelo"))
                    .andExpect(jsonPath("$.email").value("marcelo@gmail.com"));

            verify(usuarioService, times(1)).criarUsuario(usuarioRequestDTO);
        }
        @Test
        @DisplayName("Deve retornar status 400 Bad Request quando parâmetros for inválido.")
        void deveLancarExcecaoSalvarUsuario() throws Exception{
            UsuarioRequestDTO usuarioRequestDTO = new UsuarioRequestDTO("","marcelo@gmail.com");

            mockMvc.perform(post("/usuarios")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(usuarioRequestDTO)))
                    .andExpect(status().isBadRequest());

            verify(usuarioService, never()).criarUsuario(any(UsuarioRequestDTO.class));
        }
        @Test
        @DisplayName("Deve retornar status 409 Confict quando Usuário já cadastrado com esse nome ou email.")
        void deveLancarExcecaoConfrictSalvarUsuario() throws Exception{
            UsuarioRequestDTO usuarioRequestDTO = new UsuarioRequestDTO("Marcelo","marcelo@gmail.com");

            when(usuarioService.criarUsuario(usuarioRequestDTO))
                    .thenThrow(new ResponseStatusException(HttpStatus.CONFLICT,"Conflito: Usuário já cadastrado com esse nome ou email."));

            mockMvc.perform(post("/usuarios")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(usuarioRequestDTO)))
                    .andExpect(status().isConflict());

            verify(usuarioService, times(1)).criarUsuario(usuarioRequestDTO);
        }

    }

    @Nested
    @DisplayName("GET /usuarios - Buscar usuarios.")
    class buscarUsuario {
        @Test
        @DisplayName("Deve Retornar status 200 OK e a lista de usuarios.")
        void deveBuscarUsuarios() throws Exception{
            UsuarioResponseDTO usuario1 = new UsuarioResponseDTO(1L, "Marcelo", "marcelo@gmail.com");
            UsuarioResponseDTO usuario2 = new UsuarioResponseDTO(2L, "Ana", "ana@gmail.com");
            List<UsuarioResponseDTO> listaUsuarios = List.of(usuario1, usuario2);

            when(usuarioService.buscarTodosUsuarios()).thenReturn(listaUsuarios);

            // ACT & ASSERT
            mockMvc.perform(get("/usuarios")
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    // Valida se o retorno é uma lista (array JSON) com 2 elementos
                    .andExpect(jsonPath("$.length()").value(2))
                    // Valida os campos do primeiro usuário no array
                    .andExpect(jsonPath("$[0].id").value(1))
                    .andExpect(jsonPath("$[0].nome").value("Marcelo"))
                    .andExpect(jsonPath("$[0].email").value("marcelo@gmail.com"))
                    // Valida os campos do segundo usuário no array
                    .andExpect(jsonPath("$[1].id").value(2))
                    .andExpect(jsonPath("$[1].nome").value("Ana"));

            verify(usuarioService, times(1)).buscarTodosUsuarios();
        }
        @Test
        @DisplayName("Deve retornar status 200 OK e uma lista vazia quando não houver usuários")
        void deveRetornarListaVaziaQuandoNaoHouverUsuarios() throws Exception {
            when(usuarioService.buscarTodosUsuarios()).thenReturn(List.of()); // Retorna lista vazia

            mockMvc.perform(get("/usuarios")
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.length()").value(0)); // Confirma que retornou []

            verify(usuarioService, times(1)).buscarTodosUsuarios();
        }
    }
}
