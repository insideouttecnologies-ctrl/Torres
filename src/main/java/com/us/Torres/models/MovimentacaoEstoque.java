package com.us.Torres.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "movimentacao_estoque")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MovimentacaoEstoque {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    private String medicamentoId;

    @Enumerated(EnumType.STRING)
    private TipoMovimento tipoMovimento;

    private Integer quantidade;

    private String lote;

    private LocalDate dataValidade;

    private String motivo;

    private String usuarioId;

    private LocalDateTime dataHora;

    public enum TipoMovimento {
        ENTRADA,
        SAIDA,
        AJUSTE,
        PERDA
    }

    @PrePersist
    public void prePersist() {
        if (this.dataHora == null) {
            this.dataHora = LocalDateTime.now();
        }
    }
}
