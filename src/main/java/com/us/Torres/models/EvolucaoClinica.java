package com.us.Torres.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "evolucao_clinica")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EvolucaoClinica {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    private String internamentoId;

    private String profissionalId;

    private String profissionalNome;

    private String sinaisVitais;

    private String dieta;

    private String acessoVenoso;

    @Column(columnDefinition = "TEXT")
    private String conduta;

    private LocalDateTime dataHora;

    @PrePersist
    public void prePersist() {
        if (this.dataHora == null) {
            this.dataHora = LocalDateTime.now();
        }
    }
}
