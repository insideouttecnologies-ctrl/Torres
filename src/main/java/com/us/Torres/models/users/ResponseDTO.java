package com.us.Torres.models.users;

public record ResponseDTO(
        String token,
        String mensagem,
        UsuarioResponse usuario
) {
    public ResponseDTO(String token, String mensagem) {
        this(token, mensagem, null);
    }
}
