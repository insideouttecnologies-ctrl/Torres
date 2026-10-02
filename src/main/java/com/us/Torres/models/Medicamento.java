package com.us.Torres.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Table(name = "medicamento")
@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Medicamento {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    private String codigoItem;

    private String nome;

    private String principioAtivo;

    private String categoria;

    private String fabricante;

    private String codigoBarras;

    private String lote;

    private Integer quantidade;

    private Integer quantidadeMinima;

    private BigDecimal precoCompra;

    private BigDecimal precoVenda;

    private LocalDate dataFabricacao;

    private LocalDate dataValidade;

    private String dosagem;

    private String formaFarmaceutica;

    private String localizacaoAlmoxarifado;

    private String statusEstoque; // NORMAL, BAIXO, CRITICO_BAIXO

    private boolean controlado;

    private boolean ativo;

    @PrePersist
    @PreUpdate
    public void calcularStatusEstoque() {
        if (quantidade == null) {
            quantidade = 0;
        }
        if (quantidadeMinima == null) {
            quantidadeMinima = 10;
        }
        if (quantidade <= 5) {
            statusEstoque = "CRITICO_BAIXO";
        } else if (quantidade <= quantidadeMinima) {
            statusEstoque = "BAIXO";
        } else {
            statusEstoque = "NORMAL";
        }
    }
}