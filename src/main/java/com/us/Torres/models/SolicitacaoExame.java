package com.us.Torres.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "solicitacao_exame")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SolicitacaoExame {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(unique = true, nullable = false)
    private String protocolo;

    private String pacienteId;

    private String pacienteNome;

    private String medicoId;

    private String medicoNome;

    private String tipoExame;

    @Enumerated(EnumType.STRING)
    private PrioridadeExame prioridade;

    @Column(columnDefinition = "TEXT")
    private String indicacaoClinica;

    @Enumerated(EnumType.STRING)
    private StatusExame status;

    private LocalDateTime dataSolicitacao;

    public enum PrioridadeExame {
        ROTINA,
        URGENTE,
        STAT
    }

    public enum StatusExame {
        AGUARDANDO_COLETA,
        EM_ANALISE,
        LIBERADO
    }

    @PrePersist
    public void prePersist() {
        if (this.dataSolicitacao == null) {
            this.dataSolicitacao = LocalDateTime.now();
        }
        if (this.status == null) {
            this.status = StatusExame.AGUARDANDO_COLETA;
        }
        if (this.prioridade == null) {
            this.prioridade = PrioridadeExame.ROTINA;
        }
    }
}
