package com.warlley.biblioteca.controller;

import com.warlley.biblioteca.model.Usuario;
import com.warlley.biblioteca.service.UsuarioService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
public class UsuarioController {

    private final UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService){
        this.usuarioService = usuarioService;
    }

    @GetMapping("/usuarios")
    public Usuario buscarUsuario(@RequestParam Long id){
        return usuarioService.buscarUsuario(id);
    }
    @PostMapping("/usuarios")
    @ResponseStatus(HttpStatus.CREATED)
    public void salvarUsuario(@RequestBody Usuario usuario){
        usuarioService.criarUsuario(usuario);
    }
}
