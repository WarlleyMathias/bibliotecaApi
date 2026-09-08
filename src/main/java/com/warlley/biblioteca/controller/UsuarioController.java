package com.warlley.biblioteca.controller;

import com.warlley.biblioteca.dto.UsuarioRequestDTO;
import com.warlley.biblioteca.dto.UsuarioResponseDTO;
import com.warlley.biblioteca.service.UsuarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Tag(name = "Usuarios",description = "Endpoints para o gerenciamento dos usuarios")
public class UsuarioController {

    private final UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService){
        this.usuarioService = usuarioService;
    }

    @Operation(summary = "Receber uma lista de usuarios cadastrado.",description ="Busca todos os usuarios cadastrados no banco de dados.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Retorna a lista de usuarios mesmo que vazia.")
    })
    @GetMapping("/usuarios")
    public List<UsuarioResponseDTO> buscarUsuarios(){
        return usuarioService.buscarTodosUsuarios();
    }

    @Operation(summary = "Cria um novo usuario.", description = "cria um novo usuario, como os parametro nome e email.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Usuario criado com sucesso."),
            @ApiResponse(responseCode = "400", description = "Erro de validação, parametros inválidos."),
            @ApiResponse(responseCode = "409", description = "Conflito: Usuário já cadastrado com esse nome ou email.")
    })
    @PostMapping("/usuarios")
    @ResponseStatus(HttpStatus.CREATED)
    public UsuarioResponseDTO salvarUsuario(@Valid @RequestBody UsuarioRequestDTO usuarioDTO){
        return usuarioService.criarUsuario(usuarioDTO);
    }
}
