package com.us.Torres.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "configuracao_hospital")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ConfiguracaoHospital {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(nullable = false)
    private String nomeHospital;

    @Column(nullable = false)
    private String nif;

    private String telefone;

    @Column(columnDefinition = "TEXT")
    private String endereco;

    private String email;

    @Builder.Default
    private String moedaPadrao = "AOA";

    @Builder.Default
    private boolean alertaEstoqueAtivo = true;

    @Builder.Default
    private boolean doisFatoresAtivo = false;

    @Builder.Default
    private boolean auditoriaAtiva = true;

    private LocalDateTime atualizadoEm;

    @PrePersist
    @PreUpdate
    public void prePersist() {
        if (this.moedaPadrao == null || this.moedaPadrao.isBlank()) {
            this.moedaPadrao = "AOA";
        }
        if (this.atualizadoEm == null) {
            this.atualizadoEm = LocalDateTime.now();
        } else {
            this.atualizadoEm = LocalDateTime.now();
        }
    }
}
