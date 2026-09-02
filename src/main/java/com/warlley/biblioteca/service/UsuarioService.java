package com.warlley.biblioteca.service;

import com.warlley.biblioteca.model.Usuario;
import com.warlley.biblioteca.repository.UsuarioRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;


@Service
public class UsuarioService {
    UsuarioRepository usuarioRepository;

    public UsuarioService(UsuarioRepository usuarioRepository){
        this.usuarioRepository = usuarioRepository;
    }

    public Usuario buscarUsuario(Long id){
        return usuarioRepository.findById(id).orElseThrow(()
                -> new ResponseStatusException(HttpStatus.NOT_FOUND, "usuario não encontrado!"));
    }
    public void criarUsuario(Usuario usuario){
        usuarioRepository.save(usuario);
    }

}
