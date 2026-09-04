package com.warlley.biblioteca.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UsuarioRequestDTO(
        @NotBlank(message = "O nome não pode ser nulo nem em branco.")
        @Size(min = 2,max = 150,message = "O nome é obrigatório.")
        String nome,
        @NotBlank(message = "O email não pode ser Nulo nem em branco.")
        @Email(message = "O email é obrigatorio.")
        String email
) {}
