package com.us.Torres.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Table(name = "agendamento")
@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Agendamento {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    private String pacienteId;

    private String pacienteNome;

    private String medicoId;

    private String medicoNome;

    private LocalDateTime dataHora;

    private String motivo;

    @Enumerated(EnumType.STRING)
    private StatusAgendamento status;

    public enum StatusAgendamento {
        PENDENTE,
        CONFIRMADO,
        REALIZADO,
        CANCELADO
    }

    @PrePersist
    public void prePersist() {
        if (this.status == null) {
            this.status = StatusAgendamento.PENDENTE;
        }
    }
}