package com.us.Torres.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "triagem")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Triagem {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    private String senha;

    private String pacienteId;

    private String pacienteNome;

    private String enfermeiroId;

    @Enumerated(EnumType.STRING)
    private ClassificacaoRisco classificacaoRisco;

    private Integer tempoLimiteMinutos;

    private String tempoEsperaEstimado;

    private String pressaoArterial;

    private Integer saturacaoO2;

    private Double temperatura;

    private Integer frequenciaCardiaca;

    @Column(columnDefinition = "TEXT")
    private String queixaPrincipal;

    @Column(columnDefinition = "TEXT")
    private String observacoes;

    @Enumerated(EnumType.STRING)
    private StatusTriagem status;

    private LocalDateTime dataHora;

    public enum ClassificacaoRisco {
        VERMELHO,   // Imediato (0 min)
        LARANJA,    // Muito urgente (10 min)
        AMARELO,    // Urgente (60 min)
        VERDE,      // Pouco urgente (120 min)
        AZUL        // Não urgente (240 min)
    }

    public enum StatusTriagem {
        AGUARDANDO_ATENDIMENTO,
        EM_ATENDIMENTO,
        CONCLUIDA,
        CANCELADA
    }

    @PrePersist
    public void prePersist() {
        if (this.dataHora == null) {
            this.dataHora = LocalDateTime.now();
        }
        if (this.status == null) {
            this.status = StatusTriagem.AGUARDANDO_ATENDIMENTO;
        }
    }
}
