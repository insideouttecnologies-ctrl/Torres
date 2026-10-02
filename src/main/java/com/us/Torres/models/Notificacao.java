package com.us.Torres.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "notificacao")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Notificacao {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    private String usuarioDestinoId;

    private String titulo;

    @Column(columnDefinition = "TEXT")
    private String mensagem;

    private String tipo;

    @Enumerated(EnumType.STRING)
    private StatusNotificacao status;

    private LocalDateTime dataCriacao;

    public enum StatusNotificacao {
        NAO_LIDA,
        LIDA,
        ARCHIVADA
    }

    @PrePersist
    public void prePersist() {
        if (this.dataCriacao == null) {
            this.dataCriacao = LocalDateTime.now();
        }
        if (this.status == null) {
            this.status = StatusNotificacao.NAO_LIDA;
        }
    }
}
