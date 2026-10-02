package com.us.Torres.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Table(name = "consulta")
@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Consulta {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    private String pacienteId;

    private String pacienteNome;

    private String medicoId;

    private String medicoNome;

    private String especialidade;

    private LocalDateTime dataHora;

    private String motivo;

    @Column(columnDefinition = "TEXT")
    private String anamnese;

    @Column(columnDefinition = "TEXT")
    private String exameFisico;

    @Column(columnDefinition = "TEXT")
    private String diagnostico;

    @Column(columnDefinition = "TEXT")
    private String observacoes;

    private String salaConsultorio;

    private String receitaId;

    @Enumerated(EnumType.STRING)
    private StatusConsulta status;

    public enum StatusConsulta {
        AGENDADA,
        EM_ANDAMENTO,
        CONCLUIDA,
        CANCELADA
    }

    @PrePersist
    public void prePersist() {
        if (this.dataHora == null) {
            this.dataHora = LocalDateTime.now();
        }
        if (this.status == null) {
            this.status = StatusConsulta.AGENDADA;
        }
    }
}