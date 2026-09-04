package com.warlley.biblioteca.dto;

import jakarta.validation.constraints.*;

public record LivroRequestDTO(
     @NotBlank(message = "Não pode ser nulo nem em branco.")
     @Size(min = 2, max = 150, message = "O titulo é obrigatório")
     String titulo,
     @NotBlank(message = "Não pode ser nulo nem em branco.")
     @Size(min = 2, max = 150, message = "O autor é obrigatório")
     String autor,
     @NotNull(message = "Não pode ser nulo nem em branco")
     @Min(value = 1000,message = "O ano deve ter no minimo 4 digitos.")
     @Max(value = 2026,message = "O ano não pode ser no futuro.")
     int ano
){}
