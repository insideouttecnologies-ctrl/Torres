package com.us.Torres.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;

@Entity
@Table(name = "fatura")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Fatura {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    private String pacienteId;

    private String numeroFatura;

    private BigDecimal valorBruto;

    private BigDecimal valorIva;

    private BigDecimal valorTotal;

    private LocalDate dataEmissao;

    private LocalDate dataVencimento;

    @Enumerated(EnumType.STRING)
    private StatusFatura status;

    private String descricao;

    private String convenio;

    public enum StatusFatura {
        PENDENTE,
        PAGA,
        VENCIDA,
        CANCELADA
    }

    @PrePersist
    @PreUpdate
    public void calcularValores() {
        if (valorBruto == null) {
            valorBruto = BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP);
        }
        valorIva = valorBruto.multiply(new BigDecimal("0.14")).setScale(2, RoundingMode.HALF_UP);
        valorTotal = valorBruto.add(valorIva).setScale(2, RoundingMode.HALF_UP);
        if (dataEmissao == null) {
            dataEmissao = LocalDate.now();
        }
        if (dataVencimento == null) {
            dataVencimento = dataEmissao.plusDays(15);
        }
        if (status == null) {
            status = StatusFatura.PENDENTE;
        }
        if (numeroFatura == null || numeroFatura.isBlank()) {
            numeroFatura = "FAT-" + java.util.UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        }
    }
}
