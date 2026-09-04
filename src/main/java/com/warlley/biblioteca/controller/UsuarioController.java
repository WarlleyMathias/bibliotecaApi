package com.warlley.biblioteca.controller;

import com.warlley.biblioteca.dto.UsuarioRequestDTO;
import com.warlley.biblioteca.dto.UsuarioResponseDTO;
import com.warlley.biblioteca.model.Usuario;
import com.warlley.biblioteca.service.UsuarioService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class UsuarioController {

    private final UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService){
        this.usuarioService = usuarioService;
    }

    @GetMapping("/usuarios")
    public List<UsuarioResponseDTO> buscarUsuario(){
        return usuarioService.buscarTodosUsuarios();
    }
    @PostMapping("/usuarios")
    @ResponseStatus(HttpStatus.CREATED)
    public void salvarUsuario(@RequestBody UsuarioRequestDTO usuarioDTO){
        usuarioService.criarUsuario(usuarioDTO);
    }
}
