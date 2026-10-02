package com.us.Torres.models.users;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record RegisterDTO(
        @NotEmpty @NotNull @NotBlank String name,
        @NotEmpty @NotNull @NotBlank String email,
        @NotEmpty @NotNull @NotBlank String password,
        @NotEmpty @NotNull Cargo cargo,
        @NotEmpty @NotNull String codigoFuncionario) {
}
