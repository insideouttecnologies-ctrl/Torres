package com.us.Torres.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "dispensacao")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Dispensacao {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    private String receitaId;

    private String farmaceuticoId;

    private String pacienteId;

    private LocalDateTime dataDispensacao;

    private String reciboId;

    @Column(columnDefinition = "TEXT")
    private String observacao;

    @Enumerated(EnumType.STRING)
    private StatusDispensacao status;

    @ElementCollection
    @CollectionTable(name = "dispensacao_itens", joinColumns = @JoinColumn(name = "dispensacao_id"))
    private List<ItemDispensado> itens;

    public enum StatusDispensacao {
        DISPENSADO,
        CANCELADO
    }

    @Embeddable
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ItemDispensado {
        private String medicamentoId;
        private String medicamentoNome;
        private Integer quantidadeDispensada;
        private String lote;
    }

    @PrePersist
    public void prePersist() {
        if (this.dataDispensacao == null) {
            this.dataDispensacao = LocalDateTime.now();
        }
        if (this.status == null) {
            this.status = StatusDispensacao.DISPENSADO;
        }
    }
}
