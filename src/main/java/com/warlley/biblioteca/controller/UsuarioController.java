package com.warlley.biblioteca.controller;

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
    public List<Usuario> buscarUsuario(){
        return usuarioService.buscarTodosUsuarios();
    }
    @PostMapping("/usuarios")
    @ResponseStatus(HttpStatus.CREATED)
    public void salvarUsuario(@RequestBody Usuario usuario){
        usuarioService.criarUsuario(usuario);
    }
}
