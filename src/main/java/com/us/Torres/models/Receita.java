package com.us.Torres.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "receita")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Receita {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    private String pacienteId;

    private String medicoId;

    private LocalDate dataEmissao;

    @Enumerated(EnumType.STRING)
    private StatusReceita status;

    @Column(columnDefinition = "TEXT")
    private String observacoes;

    @ElementCollection
    @CollectionTable(name = "receita_itens", joinColumns = @JoinColumn(name = "receita_id"))
    private List<ItemReceita> itens;

    public enum StatusReceita {
        PENDENTE,
        DISPENSADA,
        CANCELADA
    }

    @Embeddable
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ItemReceita {
        private String medicamentoId;
        private String nome;
        private String dosagem;
        private String frequencia;
        private String duracao;
        private String instrucoes;
        private Integer quantidadeSolicitada;
    }

    @PrePersist
    @PreUpdate
    public void prePersist() {
        if (this.dataEmissao == null) {
            this.dataEmissao = LocalDate.now();
        }
        if (this.status == null) {
            this.status = StatusReceita.PENDENTE;
        }
    }
}