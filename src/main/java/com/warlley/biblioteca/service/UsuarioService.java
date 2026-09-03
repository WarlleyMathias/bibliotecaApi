package com.warlley.biblioteca.service;

import com.warlley.biblioteca.model.Usuario;
import com.warlley.biblioteca.repository.UsuarioRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;


@Service
public class UsuarioService {
    UsuarioRepository usuarioRepository;

    public UsuarioService(UsuarioRepository usuarioRepository){
        this.usuarioRepository = usuarioRepository;
    }

    public List<Usuario> buscarTodosUsuarios(){
        return usuarioRepository.findAll();
    }
    public void criarUsuario(Usuario usuario){
        if(verificaNome(usuario.getNome()) || verificaEmail(usuario.getEmail())){
            throw new ResponseStatusException(HttpStatus.CONFLICT,"já existe um usuario com esse nome ou email já cadastrado!");
        }else{
            usuarioRepository.save(usuario);
        }
    }

    public boolean verificaNome(String nome){
        return usuarioRepository.existsByNome(nome);
    }
    public boolean verificaEmail(String email){
        return usuarioRepository.existsByEmail(email);
    }

}
