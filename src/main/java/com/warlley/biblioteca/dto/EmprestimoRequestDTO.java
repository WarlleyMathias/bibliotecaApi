package com.warlley.biblioteca.dto;

import jakarta.validation.constraints.NotNull;

public record EmprestimoRequestDTO(
        @NotNull(message = "id_usuario não pode ser nulo nem em branco.")
        Long id_usuario,
        @NotNull(message = "id_livro não pode ser nulo, nem em branco.")
        Long id_livro
) {}
