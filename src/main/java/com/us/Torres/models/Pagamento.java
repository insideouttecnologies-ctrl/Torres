package com.us.Torres.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "pagamento")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Pagamento {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    private String faturaId;

    private String pacienteId;

    private BigDecimal valorPago;

    private BigDecimal valorIva;

    private BigDecimal valorTotal;

    private LocalDateTime dataPagamento;

    private String descricao;

    @Enumerated(EnumType.STRING)
    private MetodoPagamento metodo;

    @Enumerated(EnumType.STRING)
    private StatusPagamento status;

    public enum MetodoPagamento {
        DINHEIRO,
        CARTAO,
        TRANSFERENCIA,
        MULTICAIXA
    }

    public enum StatusPagamento {
        PENDENTE,
        PAGO,
        CANCELADO
    }

    @PrePersist
    public void prePersist() {
        if (this.dataPagamento == null) {
            this.dataPagamento = LocalDateTime.now();
        }
        if (this.status == null) {
            this.status = StatusPagamento.PENDENTE;
        }
    }
}