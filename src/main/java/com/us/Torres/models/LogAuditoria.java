package com.us.Torres.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "log_auditoria")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LogAuditoria {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    private String usuarioId;

    @Column(nullable = false)
    private String acao;

    @Column(nullable = false)
    private String modulo;

    private String ipOrigem;

    @Column(columnDefinition = "TEXT")
    private String detalhes;

    @Column(nullable = false)
    private LocalDateTime dataHora;

    @PrePersist
    public void prePersist() {
        if (this.dataHora == null) {
            this.dataHora = LocalDateTime.now();
        }
    }
}
