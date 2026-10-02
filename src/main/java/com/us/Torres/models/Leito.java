package com.us.Torres.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "leito")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Leito {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(unique = true, nullable = false)
    private String codigo; // ex: UTI-01, Q-103 • L-12

    private String ala; // ex: UTI Adulto, Enfermaria Masculina, etc.

    @Enumerated(EnumType.STRING)
    private StatusLeito status; // DISPONIVEL, OCUPADO, ALTA_PREVISTA, BLOQUEADO

    private String pacienteAtualId;

    private String pacienteAtualNome;

    private LocalDateTime dataAdmissao;

    private String medicoAssistente;

    public enum StatusLeito {
        DISPONIVEL,
        OCUPADO,
        ALTA_PREVISTA,
        BLOQUEADO
    }

    @PrePersist
    public void prePersist() {
        if (this.status == null) {
            this.status = StatusLeito.DISPONIVEL;
        }
    }
}
