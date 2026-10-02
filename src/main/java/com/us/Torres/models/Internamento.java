package com.us.Torres.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "internamento")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Internamento {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    private String pacienteId;

    private String pacienteNome;

    private String leitoId;

    private String leitoCodigo;

    private String ala;

    private String medicoResponsavelId;

    private String medicoResponsavelNome;

    @Column(columnDefinition = "TEXT")
    private String diagnosticoAdmissao;

    private LocalDateTime dataAdmissao;

    private LocalDateTime dataAlta;

    @Enumerated(EnumType.STRING)
    private StatusInternamento status;

    public enum StatusInternamento {
        ATIVO,
        ALTA_CONCEDIDA,
        TRANSFERIDO,
        OBITO
    }

    @PrePersist
    public void prePersist() {
        if (this.dataAdmissao == null) {
            this.dataAdmissao = LocalDateTime.now();
        }
        if (this.status == null) {
            this.status = StatusInternamento.ATIVO;
        }
    }
}
