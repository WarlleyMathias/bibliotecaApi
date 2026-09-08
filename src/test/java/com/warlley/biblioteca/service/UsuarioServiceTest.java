package com.warlley.biblioteca.service;

import com.warlley.biblioteca.dto.UsuarioRequestDTO;
import com.warlley.biblioteca.dto.UsuarioResponseDTO;
import com.warlley.biblioteca.model.Usuario;
import com.warlley.biblioteca.repository.UsuarioRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UsuarioServiceTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @InjectMocks
    private UsuarioService usuarioService;

    @Nested
    @DisplayName("Tests do método buscar todos usuarios")
    class buscarTodosUsuarios{

        @Test
        @DisplayName("Deve buscar todos os usuários do banco de dados e retornar uma lista de usuarios.")
        void deveBuscarTodosUsuarios(){
            Usuario usuario1 = new Usuario(1L,"Mathias","mathias@gmail.com");
            Usuario usuario2 = new Usuario(2L,"Marcelo","marcelo@gmail.com");
            List<Usuario> usuarioList = List.of(usuario1,usuario2);

            when(usuarioRepository.findAll()).thenReturn(usuarioList);

            List<UsuarioResponseDTO> usuarioResultado = usuarioService.buscarTodosUsuarios();

            assertNotNull(usuarioResultado);
            assertEquals(2,usuarioResultado.size());
            assertEquals(1L,usuarioResultado.get(0).id());
            assertEquals(2L,usuarioResultado.get(1).id());

            verify(usuarioRepository, times(1)).findAll();
        }
        @Test
        @DisplayName("Deve lançar uma exceção ao buscar a lista de usuarios.")
        void deveLancarUmaExcecaoAoBuscarTodosUsuarios(){
            when(usuarioRepository.findAll()).thenThrow(new RuntimeException("Erro de conexão com o banco"));

            assertThrows(RuntimeException.class, () -> usuarioService.buscarTodosUsuarios());

            verify(usuarioRepository, times(1)).findAll();
        }
    }
    @Nested
    @DisplayName("Tests do método criar usuario")
    class criarUsuario{

        @Test
        @DisplayName("Deve criar um novo usuario no banco de dados.")
        void deveCriarUsuario(){
            UsuarioRequestDTO usuarioRequestDTO = new UsuarioRequestDTO("Marcelo","marcelo@gmai.com");
            Usuario usuarioSalvo = new Usuario(1L,"Marcelo","marcelo@gmail.com");

            when(usuarioRepository.existsByNome(usuarioRequestDTO.nome())).thenReturn(false);
            when(usuarioRepository.existsByEmail(usuarioRequestDTO.email())).thenReturn(false);
            when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuarioSalvo);

            UsuarioResponseDTO usuarioResultado = usuarioService.criarUsuario(usuarioRequestDTO);

            assertNotNull(usuarioResultado);
            assertEquals(1L,usuarioResultado.id());
            assertEquals("Marcelo",usuarioResultado.nome());
            assertEquals("marcelo@gmail.com",usuarioResultado.email());

            verify(usuarioRepository,times(1)).existsByNome(usuarioRequestDTO.nome());
            verify(usuarioRepository,times(1)).existsByEmail(usuarioRequestDTO.email());
            verify(usuarioRepository,times(1)).save(usuarioSalvo);
        }
        @Test
        @DisplayName("Deve lançar uma exceção 409 ao tentar criar um novo usuario.")
        void deveLancarUmaExcecaoNomeCoflito(){
            UsuarioRequestDTO usuarioRequestDTO = new UsuarioRequestDTO("Marcelo","marcelo@gmai.com");

            when(usuarioRepository.existsByNome(usuarioRequestDTO.nome())).thenReturn(true);

            ResponseStatusException ex =
                    assertThrows(ResponseStatusException.class, () ->usuarioService.criarUsuario(usuarioRequestDTO));

            assertEquals(409,ex.getStatusCode().value());

            verify(usuarioRepository,times(1)).existsByNome(usuarioRequestDTO.nome());
            verify(usuarioRepository,never()).save(any(Usuario.class));
        }
        @Test
        @DisplayName("Deve lançar uma exceção 409 ao tentar criar um novo usuario.")
        void deveLancarUmaExcecaoEmailCoflito(){
            UsuarioRequestDTO usuarioRequestDTO = new UsuarioRequestDTO("Marcelo","marcelo@gmai.com");

            when(usuarioRepository.existsByNome(usuarioRequestDTO.email())).thenReturn(true);

            ResponseStatusException ex =
                    assertThrows(ResponseStatusException.class, () ->usuarioService.criarUsuario(usuarioRequestDTO));

            assertEquals(409,ex.getStatusCode().value());

            verify(usuarioRepository,times(1)).existsByEmail(usuarioRequestDTO.email());
            verify(usuarioRepository,never()).save(any(Usuario.class));
        }
    }

}
