package com.us.Torres.models.users;

public record UsuarioResponse(
        String id,
        String nome,
        String email,
        Cargo cargo,
        String codigoFuncionario,
        User.StatusUsuario status
) {
}